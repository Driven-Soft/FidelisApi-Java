package br.com.fiap.java.FidelisApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/v1/**", "/h2-console/**")
            )

            .headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin()) // necessário para o H2 Console
            )

            .authorizeHttpRequests(auth -> auth
                // --- Público ---
                .requestMatchers(
                    "/login", "/acesso-negado",
                    "/css/**", "/js/**", "/images/**",
                    "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                    "/h2-console/**",
                    "/actuator/health", "/actuator/info"
                ).permitAll()

                // --- API: leitura liberada para qualquer perfil autenticado ---
                .requestMatchers(HttpMethod.GET, "/api/v1/**").authenticated()

                // --- API: escrita (POST/PUT/PATCH/DELETE) só para quem gerencia ---
                .requestMatchers("/api/v1/**").hasRole("CLINICA")

                // --- Telas MVC por perfil ---
                .requestMatchers("/clinica/**").hasRole("CLINICA")
                .requestMatchers("/tutor/**").hasRole("TUTOR")
                .requestMatchers("/dashboard/**").authenticated()

                // --- Qualquer outra rota: precisa estar autenticado ---
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?falha=true")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )

            .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.sendError(jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN);
                    } else {
                        response.sendRedirect("/acesso-negado");
                    }
                })
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}