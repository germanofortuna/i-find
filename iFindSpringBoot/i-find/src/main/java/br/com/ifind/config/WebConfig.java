package br.com.ifind.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.ifind.serialization.converter.YamlJackson2HttpMessageConverter;

@Configuration //diz ao springboot que quando ele estiver subindo a aplicação ele precisa ler essa classe pois nela ele irá encontrar configurações sobre o comportamento da aplicação
public class WebConfig implements WebMvcConfigurer {
	
	private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");
	
	@Value("${cors.originPatterns:default}") //o springboot vai ler essa propriedade no application.yml e irá setar neste objeto
	private String corsOriginPatterns = "";
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new YamlJackson2HttpMessageConverter());
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		var allowedOrigin = corsOriginPatterns.split(",");
		registry.addMapping("/**") //todas as rotas da API
			//.allowedMethods("GET", "POST", "PUT") //pertmite para os métodos declarados
			.allowedMethods("*") //permite para todos
			.allowedOrigins(allowedOrigin)
		.allowCredentials(true); //possibilitar autenticação
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		
		/* content negotiations using Query params
		configurer.favorParameter(true)
			.parameterName("mediaType")
			.ignoreAcceptHeader(true)
			.useRegisteredExtensionsOnly(false)
			.defaultContentType(MediaType.APPLICATION_JSON)
				.mediaType("json", MediaType.APPLICATION_JSON)
				.mediaType("xml", MediaType.APPLICATION_XML);
				*/
		configurer.favorParameter(false)
		.ignoreAcceptHeader(false)
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
			.mediaType("json", MediaType.APPLICATION_JSON)
			.mediaType("xml", MediaType.APPLICATION_XML)
			.mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);
	}
	
}
