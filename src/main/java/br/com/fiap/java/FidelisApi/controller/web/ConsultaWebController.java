package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes;
import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Recomendacao;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.mapper.ConsultaMapper;
import br.com.fiap.java.FidelisApi.repository.ConsultaRepository;
import br.com.fiap.java.FidelisApi.repository.LembreteRepository;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.RecomendacaoRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import br.com.fiap.java.FidelisApi.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/clinica/consultas")
@RequiredArgsConstructor
public class ConsultaWebController {

    private final ConsultaService consultaService;
    private final ConsultaRepository consultaRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final LembreteRepository lembreteRepository;
    private final RecomendacaoRepository recomendacaoRepository;
    private final Validator validator;

    @GetMapping("/pet/{petId}")
    public String listarPorPet(@PathVariable Long petId, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Pet pet = petDaClinica(petId, clinicaId);

        model.addAttribute("pet", pet);
        model.addAttribute("consultas", consultaRepository.findByPetIdComVeterinario(petId));
        return "clinica/consultas-lista";
    }

    @GetMapping("/nova")
    public String novaForm(@RequestParam(required = false) Long petId,
                            Authentication authentication,
                            Model model) {

        Long clinicaId = clinicaLogada(authentication).getId();

        ConsultaRequest request = new ConsultaRequest();
        request.setPetId(petId);

        prepararFormulario(clinicaId, request, model);
        return "clinica/consultas-form";
    }

    @PostMapping
    public String registrar(@ModelAttribute("consultaRequest") ConsultaRequest request,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        Long clinicaId = clinicaLogada(authentication).getId();
        validator.validate(request, bindingResult);

        if (!bindingResult.hasErrors()) {
            validarVinculos(request, clinicaId, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            prepararFormulario(clinicaId, request, model);
            return "clinica/consultas-form";
        }

        Consulta consulta = consultaService.create(
                ConsultaMapper.toEntity(request), request.getVeterinarioId(), request.getPetId());

        return "redirect:/clinica/consultas/" + consulta.getId() + "/confirmacao";
    }

    @GetMapping("/{id}/confirmacao")
    public String confirmacao(@PathVariable Long id, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Consulta consulta = consultaDaClinica(id, clinicaId);

        Lembrete lembrete = lembreteRepository.findFirstByPetIdOrderByIdDesc(consulta.getPet().getId())
                .orElse(null);
        Recomendacao recomendacao = recomendacaoRepository.findFirstByPetIdOrderByIdDesc(consulta.getPet().getId())
                .orElse(null);

        model.addAttribute("consulta", consulta);
        model.addAttribute("lembrete", lembrete);
        model.addAttribute("recomendacao", recomendacao);
        return "clinica/consultas-confirmacao";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Consulta consulta = consultaDaClinica(id, clinicaId);

        prepararEdicao(clinicaId, id, consulta.getPet(), ConsultaMapper.toRequest(consulta), model);
        return "clinica/consultas-form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                             @ModelAttribute("consultaRequest") ConsultaRequest request,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        Long clinicaId = clinicaLogada(authentication).getId();
        Consulta consulta = consultaDaClinica(id, clinicaId);
        Pet pet = consulta.getPet();

        request.setPetId(pet.getId());
        validator.validate(request, bindingResult);

        if (!bindingResult.hasErrors()) {
            validarVeterinario(request.getVeterinarioId(), clinicaId, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            prepararEdicao(clinicaId, id, pet, request, model);
            return "clinica/consultas-form";
        }

        consultaService.update(id, ConsultaMapper.toEntity(request), request.getVeterinarioId());

        return "redirect:/clinica/consultas/pet/" + pet.getId() + "?sucesso=atualizada";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Authentication authentication) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Long petId = consultaDaClinica(id, clinicaId).getPet().getId();

        consultaService.delete(id);

        return "redirect:/clinica/consultas/pet/" + petId + "?sucesso=excluida";
    }

    private void prepararFormulario(Long clinicaId, ConsultaRequest request, Model model) {
        model.addAttribute("consultaRequest", request);
        model.addAttribute("dataMinimaRetorno", LocalDate.now());
        model.addAttribute("tiposConsulta", CatalogoOpcoes.tiposConsulta());
        model.addAttribute("pets", petRepository.findByClinicaIdComTutor(clinicaId, PageRequest.of(0, 100, Sort.by("nome"))));
        model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
    }

    private void prepararEdicao(Long clinicaId, Long consultaId, Pet pet, ConsultaRequest request, Model model) {
        model.addAttribute("consultaRequest", request);
        model.addAttribute("consultaId", consultaId);
        model.addAttribute("petNome", pet.getNome());
        model.addAttribute("dataMinimaRetorno", LocalDate.now());
        model.addAttribute("tiposConsulta", CatalogoOpcoes.tiposConsulta());
        model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
    }

    private void validarVinculos(ConsultaRequest request, Long clinicaId, BindingResult bindingResult) {
        if (petRepository.findByIdAndClinicaId(request.getPetId(), clinicaId).isEmpty()) {
            bindingResult.rejectValue("petId", "consulta.pet.clinica", "O pet não pertence à clínica logada");
        }
        validarVeterinario(request.getVeterinarioId(), clinicaId, bindingResult);
    }

    private void validarVeterinario(Long veterinarioId, Long clinicaId, BindingResult bindingResult) {
        if (veterinarioRepository.findByIdAndClinicaId(veterinarioId, clinicaId).isEmpty()) {
            bindingResult.rejectValue("veterinarioId", "consulta.veterinario.clinica", "O veterinário não pertence à clínica logada");
        }
    }

    private Consulta consultaDaClinica(Long id, Long clinicaId) {
        return consultaRepository.findByIdAndPetClinicaId(id, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Consulta não pertence à clínica logada"));
    }

    private Pet petDaClinica(Long petId, Long clinicaId) {
        return petRepository.findByIdAndClinicaId(petId, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Pet não pertence à clínica logada"));
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getClinica();
    }
}