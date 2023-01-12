package br.com.ifind.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	//Bean: objeto montado, instanciado e gerenciado pelo Spring AOC container
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("RESTful API with Spring Boot")
						.version("v1")
						.description("Just for learning purposes")
						.termsOfService("https://springdoc.org/")
						.license(
								new License()
									.name("Apache 2.0")
									.url("https://springdoc.org/")
								)
						);
	}
	
}
