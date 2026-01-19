package com.ecommerce_backend.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtFilter jwtFilter;
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        // ✅ Public APIs
                        .requestMatchers(
                                "/api/v1/user/register",
                                "/api/v1/user/login"
                        ).permitAll()

                        // ✅ USER + ADMIN
                        .requestMatchers(
                                "/api/v1/user/list",
                                "/api/v1/user/{id}"
                        ).hasAnyRole("USER", "ADMIN")


                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/delete/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/update/**")
                        .hasAnyRole("USER", "ADMIN")



                        // 🔐 ADMIN ONLY
                     //   .requestMatchers("/api/v1/user/delete/**")
                    //    .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }




}
