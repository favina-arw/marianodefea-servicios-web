package com.marianodefea.servicios_web.security.config;

import com.marianodefea.servicios_web.service.UserDetailsServiceImp;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @PostConstruct
    public void init() {
        System.out.println("🔐 SecurityConfig ha sido cargada correctamente");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Agregué "/webjars/**" aquí abajo
                        .requestMatchers("/", "/public/**", "/login", "/register", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/fichada/**").permitAll()
                        .requestMatchers("/agentes/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // Comentamos o eliminamos httpBasic si quieres usar solo formularios
                // .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureHandler((request, response, exception) -> {
                            // Capturamos el tipo de excepción para dar un mensaje claro
                            String message = "Usuario o contraseña incorrectos.";
                            if (exception instanceof LockedException) {
                                message = "Tu cuenta está bloqueada. Contacta a un administrador.";
                            }
                            // Redirigimos pasando el mensaje codificado para que no se rompa la URL
                            String encodedMessage = java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
                            response.sendRedirect("/login?errorMessage=" + encodedMessage);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImp();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider (UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }



}
