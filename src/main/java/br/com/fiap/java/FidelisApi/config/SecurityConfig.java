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
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LoginSucessoHandler loginSucessoHandler) throws Exception {

        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/v1/**", "/h2-console/**")
            )

            .headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin())
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/home",
                    "/login", "/acesso-negado",
                    "/css/**", "/js/**", "/images/**",
                    "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                    "/h2-console/**",
                    "/actuator/health", "/actuator/info"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/**").hasRole("CLINICA")

                .requestMatchers("/api/v1/**").hasRole("CLINICA")

                .requestMatchers("/clinica/**").hasRole("CLINICA")
                .requestMatchers("/tutor/**").hasRole("TUTOR")
                .requestMatchers("/dashboard/**").authenticated()

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .successHandler(loginSucessoHandler)
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