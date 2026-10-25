package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes;
import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.mapper.VacinacaoMapper;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.repository.VacinacaoRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import br.com.fiap.java.FidelisApi.service.VacinacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clinica/vacinacoes")
@RequiredArgsConstructor
public class VacinacaoWebController {

    private final VacinacaoService vacinacaoService;
    private final VacinacaoRepository vacinacaoRepository;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final Validator validator;

    @GetMapping("/pet/{petId}")
    public String listarPorPet(@PathVariable Long petId, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Pet pet = petDaClinica(petId, clinicaId);

        model.addAttribute("pet", pet);
        model.addAttribute("vacinacoes", vacinacaoRepository.findByPetIdComVeterinario(petId));
        return "clinica/vacinacoes-lista";
    }

    @GetMapping("/nova")
    public String novaForm(@RequestParam(required = false) Long petId,
                           Authentication authentication,
                           Model model) {
        Clinica clinica = clinicaLogada(authentication);
        VacinacaoRequest request = new VacinacaoRequest();
        request.setPetId(petId);
        prepararFormulario(clinica.getId(), request, model);
        return "clinica/vacinacoes-form";
    }

    @PostMapping
    public String registrar(@ModelAttribute("vacinacaoRequest") VacinacaoRequest request,
                            BindingResult bindingResult,
                            Authentication authentication,
                            Model model) {
        Clinica clinica = clinicaLogada(authentication);
        validator.validate(request, bindingResult);

        if (!bindingResult.hasErrors()) {
            validarVinculos(request, clinica.getId(), bindingResult);
        }

        if (bindingResult.hasErrors()) {
            prepararFormulario(clinica.getId(), request, model);
            return "clinica/vacinacoes-form";
        }

        vacinacaoService.create(
                VacinacaoMapper.toEntity(request), request.getPetId(), request.getVeterinarioId());

        return "redirect:/clinica/pets?sucesso=vacinacao";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Vacinacao vacinacao = vacinacaoDaClinica(id, clinicaId);

        prepararEdicao(clinicaId, id, vacinacao.getPet(), VacinacaoMapper.toRequest(vacinacao), model);
        return "clinica/vacinacoes-form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute("vacinacaoRequest") VacinacaoRequest request,
                            BindingResult bindingResult,
                            Authentication authentication,
                            Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Vacinacao vacinacao = vacinacaoDaClinica(id, clinicaId);
        Pet pet = vacinacao.getPet();

        request.setPetId(pet.getId());
        validator.validate(request, bindingResult);

        if (!bindingResult.hasErrors()) {
            validarVeterinario(request.getVeterinarioId(), clinicaId, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            prepararEdicao(clinicaId, id, pet, request, model);
            return "clinica/vacinacoes-form";
        }

        vacinacaoService.update(id, VacinacaoMapper.toEntity(request), request.getVeterinarioId());

        return "redirect:/clinica/vacinacoes/pet/" + pet.getId() + "?sucesso=atualizada";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Authentication authentication) {
        Long clinicaId = clinicaLogada(authentication).getId();
        Long petId = vacinacaoDaClinica(id, clinicaId).getPet().getId();

        vacinacaoService.delete(id);

        return "redirect:/clinica/vacinacoes/pet/" + petId + "?sucesso=excluida";
    }

    private void prepararFormulario(Long clinicaId, VacinacaoRequest request, Model model) {
        Page<Pet> pets = petRepository.findByClinicaIdComTutor(
                clinicaId, PageRequest.of(0, 100, Sort.by("nome")));

        model.addAttribute("vacinacaoRequest", request);
        model.addAttribute("dataMaximaVacinacao", LocalDate.now());
        model.addAttribute("dataMinimaProximaDose", LocalDate.now());
        model.addAttribute("vacinas", CatalogoOpcoes.vacinas());
        model.addAttribute("pets", pets);
        model.addAttribute("gruposPorPet", pets.stream().collect(Collectors.toMap(
                Pet::getId, pet -> CatalogoOpcoes.grupoDaEspecie(pet.getEspecie()))));
        model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
    }

    private void prepararEdicao(Long clinicaId, Long vacinacaoId, Pet pet, VacinacaoRequest request, Model model) {
        model.addAttribute("vacinacaoRequest", request);
        model.addAttribute("vacinacaoId", vacinacaoId);
        model.addAttribute("petNome", pet.getNome());
        model.addAttribute("grupoPet", CatalogoOpcoes.grupoDaEspecie(pet.getEspecie()));
        model.addAttribute("dataMaximaVacinacao", LocalDate.now());
        model.addAttribute("dataMinimaProximaDose",
                request.getDataAplicacao() != null ? request.getDataAplicacao() : LocalDate.now());
        model.addAttribute("vacinas", CatalogoOpcoes.vacinas());
        model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
    }

    private void validarVinculos(VacinacaoRequest request, Long clinicaId, BindingResult bindingResult) {
        if (petRepository.findByIdAndClinicaId(request.getPetId(), clinicaId).isEmpty()) {
            bindingResult.rejectValue("petId", "vacinacao.pet.clinica", "O pet não pertence à clínica logada");
        }
        validarVeterinario(request.getVeterinarioId(), clinicaId, bindingResult);
    }

    private void validarVeterinario(Long veterinarioId, Long clinicaId, BindingResult bindingResult) {
        if (veterinarioRepository.findByIdAndClinicaId(veterinarioId, clinicaId).isEmpty()) {
            bindingResult.rejectValue("veterinarioId", "vacinacao.veterinario.clinica", "O veterinário não pertence à clínica logada");
        }
    }

    private Vacinacao vacinacaoDaClinica(Long id, Long clinicaId) {
        return vacinacaoRepository.findByIdAndPetClinicaId(id, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Vacinação não pertence à clínica logada"));
    }

    private Pet petDaClinica(Long petId, Long clinicaId) {
        return petRepository.findByIdAndClinicaId(petId, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Pet não pertence à clínica logada"));
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        if (usuario.getClinica() == null) {
            throw new AccessDeniedException("Usuário sem clínica vinculada");
        }
        return usuario.getClinica();
    }
}