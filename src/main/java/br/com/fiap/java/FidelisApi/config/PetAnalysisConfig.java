package br.com.fiap.java.FidelisApi.config;

import br.com.fiap.java.FidelisApi.service.PetAnalysisService;
import br.com.fiap.java.FidelisApi.service.PetAnaliseContextService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PetAnalysisConfig {

    @Bean
    @ConditionalOnProperty(prefix = "fidelis.analise.ia", name = "enabled", havingValue = "true")
    PetAnalysisService petAnalysisService(
            ChatClient.Builder chatClientBuilder,
            PetAnaliseContextService contextoService,
            @Value("${spring.ai.openai.api-key:}") String apiKey
    ) {
        if (apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Defina OPENAI_API_KEY antes de habilitar a análise de pets com IA.");
        }
        return new PetAnalysisService(chatClientBuilder, contextoService);
    }
}
