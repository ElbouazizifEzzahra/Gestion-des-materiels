package com.rouandi.rouandi.config;


import com.rouandi.rouandi.jwt.JwtAuthentificationFilter;
import com.rouandi.rouandi.services.UtilisateurDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig  {


    private UtilisateurDetails userDetailsService;
    private JwtAuthentificationFilter jwtAuthentificationFilter;
    public SecurityConfig(UtilisateurDetails userDetailsService, JwtAuthentificationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthentificationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http, JwtAuthentificationFilter jwtAuthenticationFilter) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/demandes/**").permitAll()
                        .requestMatchers("/materiel/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/lignes-demandes/**").permitAll()
                        .requestMatchers("/destinataire/**").permitAll()
                        .requestMatchers("/auth/si/**").hasAuthority("ROLE_ESI")
                        .requestMatchers("/auth/user/**").hasAuthority("ROLE_E")
                        .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")

                        .anyRequest().authenticated()

                )
               .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )
                .authenticationProvider(authenticationProvider())
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .build();
    }


    @Bean

    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
