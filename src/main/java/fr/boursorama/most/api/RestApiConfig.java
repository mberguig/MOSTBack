package fr.boursorama.most.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class RestApiConfig {

	@Bean
	public Docket apiDocs() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("api").apiInfo(apiInfo()).select()
				.paths(PathSelectors.ant("/api/**")).build();
	}

	@Bean
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("MOST API").description("MOST REST API").version("0.1").build();
	}

	@Bean
	public Docket monitorDocs() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("monitor").apiInfo(monitorInfo()).select()
				.paths(PathSelectors.ant("/monitor/**")).build();
	}

	@Bean
	public ApiInfo monitorInfo() {
		return new ApiInfoBuilder().title("Spring actuator API").description("Spring actuator REST API").version("3.5")
				.build();
	}
}
