package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.dto.request.PetRequest;
import br.com.fiap.java.FidelisApi.entity.Clinica;
import br.com.fiap.java.FidelisApi.entity.Pet;
import br.com.fiap.java.FidelisApi.entity.Usuario;
import br.com.fiap.java.FidelisApi.mapper.PetMapper;
import br.com.fiap.java.FidelisApi.repository.TutorRepository;
import br.com.fiap.java.FidelisApi.repository.UsuarioRepository;
import br.com.fiap.java.FidelisApi.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/clinica/pets")
@RequiredArgsConstructor
public class PetWebController {

    private final PetService petService;
    private final TutorRepository tutorRepository;
    private final UsuarioRepository usuarioRepository;
    private final Validator validator;

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        Long clinicaId = clinicaLogada(authentication).getId();
        var pets = petService.findAllByClinica(clinicaId, PageRequest.of(0, 100, Sort.by("nome")));
        model.addAttribute("pets", pets);
        return "clinica/pets-lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("petRequest", new PetRequest());
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("dataMinimaNascimento", LocalDate.now().minusYears(100));
        model.addAttribute("dataMaximaNascimento", LocalDate.now());
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
            model.addAttribute("tutores", tutorRepository.findAll());
            return "clinica/pets-form";
        }

        Pet pet = PetMapper.toEntity(petRequest);
        petService.create(pet, petRequest.getTutorId(), petRequest.getClinicaId());

        return "redirect:/clinica/pets";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Pet pet = petService.findById(id);

        PetRequest request = new PetRequest();
        request.setNome(pet.getNome());
        request.setEspecie(pet.getEspecie());
        request.setRaca(pet.getRaca());
        request.setSexo(pet.getSexo());
        request.setDataNascimento(pet.getDataNascimento());
        request.setStatus(pet.getStatus());
        request.setFotoUrl(pet.getFotoUrl());
        request.setTutorId(pet.getTutor().getId());

        model.addAttribute("petId", id);
        model.addAttribute("petRequest", request);
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("dataMinimaNascimento", LocalDate.now().minusYears(100));
        model.addAttribute("dataMaximaNascimento", LocalDate.now());
        return "clinica/pets-form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                             @ModelAttribute("petRequest") PetRequest petRequest,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {

        petRequest.setClinicaId(clinicaLogada(authentication).getId());
        validator.validate(petRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("petId", id);
            model.addAttribute("tutores", tutorRepository.findAll());
            return "clinica/pets-form";
        }

        Pet update = PetMapper.toEntity(petRequest);
        petService.update(id, update);

        return "redirect:/clinica/pets";
    }

    private Clinica clinicaLogada(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmailComVinculo(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));
        return usuario.getClinica();
    }
}