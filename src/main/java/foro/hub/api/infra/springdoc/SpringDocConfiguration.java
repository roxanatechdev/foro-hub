package foro.hub.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SpringDocConfiguration {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearer-key",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
        .info(new Info()
            .title("FORO HUB API REST")
            .description("API REST de FORO HUB, que permite la gestión eficiente de usuarios y la interacción en foros de discusión. Con funcionalidades para crear y administrar tópicos y respuestas, esta API está diseñada para fomentar la colaboración y el intercambio de ideas en un entorno digital.")
            .contact(new Contact()
                .name("Equipo Backend")
                .email("backend@foro.hub"))
            .license(new License()
                .name("Apache 2.0")
                .url("http://foro.hub/api/licencia")));
  }


}