package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.dto.request.ConsultaRequest;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Consulta;
import br.com.fiap.java.FidelisApi.entity.Lembrete;
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
import java.time.LocalDateTime;

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

    @GetMapping("/nova")
    public String novaForm(@RequestParam(required = false) Long petId,
                            Authentication authentication,
                            Model model) {

        Long clinicaId = clinicaLogada(authentication).getId();

        ConsultaRequest request = new ConsultaRequest();
        request.setPetId(petId);

        model.addAttribute("consultaRequest", request);
        model.addAttribute("dataHoraMinimaConsulta", LocalDateTime.now().withSecond(0).withNano(0));
        model.addAttribute("dataMinimaRetorno", LocalDate.now());
        model.addAttribute("pets", petRepository.findByClinicaIdComTutor(clinicaId, PageRequest.of(0, 100, Sort.by("nome"))));
        model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
        return "clinica/consultas-form";
    }

    @PostMapping
    public String registrar(@ModelAttribute("consultaRequest") ConsultaRequest request,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            Long clinicaId = clinicaLogada(authentication).getId();
            model.addAttribute("dataHoraMinimaConsulta", LocalDateTime.now().withSecond(0).withNano(0));
            model.addAttribute("dataMinimaRetorno", LocalDate.now());
            model.addAttribute("pets", petRepository.findByClinicaIdComTutor(clinicaId, PageRequest.of(0, 100, Sort.by("nome"))));
            model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
            return "clinica/consultas-form";
        }

        Consulta consulta = consultaService.create(
                ConsultaMapper.toEntity(request), request.getVeterinarioId(), request.getPetId());

        return "redirect:/clinica/consultas/" + consulta.getId() + "/confirmacao";
    }

    @GetMapping("/{id}/confirmacao")
    public String confirmacao(@PathVariable Long id, Model model) {
        Consulta consulta = consultaRepository.findByIdComPetEVeterinario(id)
                .orElseThrow(() -> new IllegalStateException("Consulta não encontrada: " + id));

        Lembrete lembrete = lembreteRepository.findFirstByPetIdOrderByIdDesc(consulta.getPet().getId())
                .orElse(null);
        Recomendacao recomendacao = recomendacaoRepository.findFirstByPetIdOrderByIdDesc(consulta.getPet().getId())
                .orElse(null);

        model.addAttribute("consulta", consulta);
        model.addAttribute("lembrete", lembrete);
        model.addAttribute("recomendacao", recomendacao);
        return "clinica/consultas-confirmacao";
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getClinica();
    }
}