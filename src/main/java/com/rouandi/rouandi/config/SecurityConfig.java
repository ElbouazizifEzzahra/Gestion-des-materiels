package com.rouandi.rouandi.config;


import com.rouandi.rouandi.jwt.JwtAuthentificationFilter;
import com.rouandi.rouandi.services.Commande.User.UtilisateurDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/demandes/**").hasAnyAuthority("ADMIN","ESI","E")
                        /*.requestMatchers("/demandes/**").permitAll()
                        .requestMatchers("/materiel/**").permitAll()*/
                        .requestMatchers("/materiel/**").hasAnyAuthority("ADMIN","E")
                       .requestMatchers("/user/**").hasAnyAuthority("ADMIN","ESI","E")
                        //.requestMatchers("/user/**").permitAll()
                        .requestMatchers("/lignes-demandes/**").hasAnyAuthority("ESI","E")
                        .requestMatchers("/destinataire/**").hasAuthority("ADMIN")
                        .requestMatchers("/decharges/**").hasAnyAuthority("ADMIN","ESI","E")
                        .requestMatchers("/uplaods/**").hasAnyAuthority("ADMIN","ESI","E")
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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
