package br.com.fiap.java.FidelisApi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DatasVacinacaoValidasValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface DatasVacinacaoValidas {

    String message() default "A próxima dose não pode ser anterior à data de aplicação";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
