package io.ionuth.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication( exclude = 
	{SecurityAutoConfiguration.class} )
public class InvoiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceBackendApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}

}
