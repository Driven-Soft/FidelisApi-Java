package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.common.CatalogoOpcoes;
import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.mapper.PetMapper;
import br.com.fiap.java.FidelisApi.repository.PetRepository;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.service.PetService;
import br.com.fiap.java.FidelisApi.service.RetencaoService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clinica/pets")
@RequiredArgsConstructor
public class PetWebController {

    private final PetService petService;
    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;
    private final UsuarioRepository usuarioRepository;
    private final RetencaoService retencaoService;
    private final Validator validator;

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        var pets = petService.findAllByClinica(clinicaId, PageRequest.of(0, 100, Sort.by("nome")));
        List<RetencaoService.PetEmRisco> emRisco = retencaoService.listarPetsEmRisco(clinicaId);

        model.addAttribute("pets", pets);
        model.addAttribute("qtdPetsEmRisco", emRisco.size());
        model.addAttribute("idsPetsEmRisco",
                emRisco.stream().map(item -> item.pet().getId()).collect(Collectors.toSet()));
        return "clinica/pets-lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("petRequest", new PetRequest());
        prepararFormulario(model);
        return "clinica/pets-form";
    }

    @PostMapping
    public String salvar(@ModelAttribute("petRequest") PetRequest petRequest,
                          BindingResult bindingResult,
                          Authentication authentication,
                          Model model) {

        petRequest.setClinicaId(clinicaLogada(authentication).getId());
        validator.validate(petRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            prepararFormulario(model);
            return "clinica/pets-form";
        }

        Pet pet = PetMapper.toEntity(petRequest);
        petService.create(pet, petRequest.getTutorId(), petRequest.getClinicaId());

        return "redirect:/clinica/pets";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Authentication authentication, Model model) {
        Pet pet = petDaClinica(id, clinicaLogada(authentication).getId());

        PetRequest request = new PetRequest();
        request.setNome(pet.getNome());
        request.setEspecie(pet.getEspecie().getRotulo());
        request.setRaca(pet.getRaca());
        request.setSexo(pet.getSexo());
        request.setDataNascimento(pet.getDataNascimento());
        request.setStatus(pet.getStatus());
        request.setFotoUrl(pet.getFotoUrl());
        request.setTutorId(pet.getTutor().getId());

        model.addAttribute("petId", id);
        model.addAttribute("petRequest", request);
        prepararFormulario(model);
        return "clinica/pets-form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                             @ModelAttribute("petRequest") PetRequest petRequest,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        Long clinicaId = clinicaLogada(authentication).getId();
        petDaClinica(id, clinicaId);

        petRequest.setClinicaId(clinicaId);
        validator.validate(petRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("petId", id);
            prepararFormulario(model);
            return "clinica/pets-form";
        }

        Pet update = PetMapper.toEntity(petRequest);
        petService.update(id, update);

        return "redirect:/clinica/pets";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, Authentication authentication) {
        petDaClinica(id, clinicaLogada(authentication).getId());

        petService.delete(id);

        return "redirect:/clinica/pets?sucesso=pet-excluido";
    }

    private void prepararFormulario(Model model) {
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("especies", CatalogoOpcoes.especies());
        model.addAttribute("racas", CatalogoOpcoes.racas());
        model.addAttribute("dataMinimaNascimento", LocalDate.now().minusYears(100));
        model.addAttribute("dataMaximaNascimento", LocalDate.now());
    }

    private Pet petDaClinica(Long id, Long clinicaId) {
        return petRepository.findByIdAndClinicaId(id, clinicaId)
                .orElseThrow(() -> new AccessDeniedException("Pet não pertence à clínica logada"));
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getClinica();
    }
}