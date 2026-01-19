package com.ecommerce_backend.security;

import com.ecommerce_backend.entity.User;
import com.ecommerce_backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


    @Component
    public class JwtFilter  extends OncePerRequestFilter {


        @Autowired
        private JwtService jwtService;
        @Autowired
        private UserRepository userRepository;


        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                String username = jwtService.validateTokenAndRetrieveSubject(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.singleton(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + user.getRole().name()
                                            )
                                    )
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);


                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);


                }


            }

            filterChain.doFilter(request, response);
        }

    }

