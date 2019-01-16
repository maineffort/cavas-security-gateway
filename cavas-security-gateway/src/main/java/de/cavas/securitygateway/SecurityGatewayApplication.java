package de.cavas.securitygateway;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


@EnableAutoConfiguration
@SpringBootApplication
//@EnableEurekaServer
public class SecurityGatewayApplication {

	public static void main(String[] args) {
		EurekaServerConfiguration eurekaServerConfiguration;
		SpringApplication.run(SecurityGatewayApplication.class, args);
	}
	
	
	@Bean
	public Filter webRequestLoggingFilter() {
	    return new CommonsRequestLoggingFilter();
	}

}

