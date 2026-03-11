package foro.hub.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

  @Autowired
  SecurityFilter securityFilter;
  //quitar la seguridad(login) por defecto
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configurar sesiones sin estado
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permitir acceso publico a /login
            .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()//perimitir acceso publico a usuarios
            .requestMatchers("/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**").permitAll()//para mostrar doumentacion
            .anyRequest().authenticated() // Requiere autenticación para las demás rutas
        )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); // Añadir filtro personalizado

    return httpSecurity.build(); // Construir la configuración
  }


  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}