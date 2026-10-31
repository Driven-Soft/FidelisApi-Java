package br.com.fiap.java.FidelisApi.controller.web;

import br.com.fiap.java.FidelisApi.dto.request.TutorRequest;
import br.com.fiap.java.FidelisApi.exception.BusinessException;
import br.com.fiap.java.FidelisApi.mapper.TutorMapper;
import br.com.fiap.java.FidelisApi.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/cadastro/tutor")
@RequiredArgsConstructor
public class TutorCadastroWebController {

    private final TutorService tutorService;
    private final Validator validator;

    @GetMapping
    public String formulario(Model model) {
        model.addAttribute("tutorRequest", new TutorRequest());
        return "cadastro-tutor";
    }

    @PostMapping
    public String cadastrar(@ModelAttribute("tutorRequest") TutorRequest request,
                            BindingResult bindingResult,
                            Model model) {
        request.setDataCriacao(LocalDate.now());
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            return "cadastro-tutor";
        }

        try {
            tutorService.create(TutorMapper.toEntity(request));
        } catch (BusinessException exception) {
            if (exception.getMessage().contains("CPF")) {
                bindingResult.rejectValue("cpf", "tutor.cpf.duplicado", exception.getMessage());
            } else if (exception.getMessage().contains("Email")) {
                bindingResult.rejectValue("email", "tutor.email.duplicado", exception.getMessage());
            } else {
                throw exception;
            }
            return "cadastro-tutor";
        }

        return "redirect:/login?cadastro=sucesso";
    }
}
