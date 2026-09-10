package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.dto.request.VacinacaoRequest;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.entity.Vacinacao;
import br.com.fiap.java.FidelisApi.entity.Veterinario;
import br.com.fiap.java.FidelisApi.exception.ResourceNotFoundException;
import br.com.fiap.java.FidelisApi.mapper.VacinacaoMapper;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.repository.VeterinarioRepository;
import br.com.fiap.java.FidelisApi.service.VacinacaoService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/clinica/vacinacoes")
@RequiredArgsConstructor
public class VacinacaoWebController {

    private final VacinacaoService vacinacaoService;
    private final PetRepository petRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final Validator validator;

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

        Vacinacao vacinacao = vacinacaoService.create(
                VacinacaoMapper.toEntity(request), request.getPetId(), request.getVeterinarioId());

        return "redirect:/clinica/pets?sucesso=vacinacao";
    }

    private void prepararFormulario(Long clinicaId, VacinacaoRequest request, Model model) {
        model.addAttribute("vacinacaoRequest", request);
        model.addAttribute("pets", petRepository.findByClinicaIdComTutor(
                clinicaId, PageRequest.of(0, 100, Sort.by("nome"))));
        model.addAttribute("veterinarios", veterinarioRepository.findByClinicaId(clinicaId));
    }

    private void validarVinculos(VacinacaoRequest request, Long clinicaId, BindingResult bindingResult) {
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet não encontrado"));
        Veterinario veterinario = veterinarioRepository.findById(request.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinário não encontrado"));

        if (!clinicaId.equals(pet.getClinica().getId())) {
            bindingResult.rejectValue("petId", "vacinacao.pet.clinica", "O pet não pertence à clínica logada");
        }
        if (!clinicaId.equals(veterinario.getClinica().getId())) {
            bindingResult.rejectValue("veterinarioId", "vacinacao.veterinario.clinica", "O veterinário não pertence à clínica logada");
        }
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
