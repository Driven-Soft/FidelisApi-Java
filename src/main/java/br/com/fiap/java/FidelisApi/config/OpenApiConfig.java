package br.com.fiap.java.FidelisApi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fidelisOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fidelis Vet API")
                        .version("1.0.0")
                        .description("API REST para gestão clínica veterinária com base no modelo Fidelis Vet.")
                        .contact(new Contact().name("Fidelis Vet Team").email("contato@fidelisvet.com.br"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação do projeto")
                        .url("https://github.com/Driven-Soft/FidelisApi-Java"));
    }
}
