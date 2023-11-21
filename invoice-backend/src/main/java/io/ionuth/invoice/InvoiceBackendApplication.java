package io.ionuth.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootApplication( exclude = {SecurityAutoConfiguration.class} )
@SpringBootApplication
public class InvoiceBackendApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(InvoiceBackendApplication.class, args);
		BCryptPasswordEncoder passEncoder = ctx.getBean(BCryptPasswordEncoder.class);
		System.out.println("123456 encoded: " + passEncoder.encode("123456"));
	}
	
}
