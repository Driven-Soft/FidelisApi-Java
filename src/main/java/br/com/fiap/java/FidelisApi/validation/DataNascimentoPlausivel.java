package br.com.fiap.java.FidelisApi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DataNascimentoPlausivelValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface DataNascimentoPlausivel {

    String message() default "Data de nascimento excede a idade máxima para a espécie informada";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
