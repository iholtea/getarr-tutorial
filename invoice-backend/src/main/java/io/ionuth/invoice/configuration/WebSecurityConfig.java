package io.ionuth.invoice.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		/*
		 *  PathRequest.toH2Console() is equivalent to 
		 *  new AntPathRequestMatcher("/h2-console/**") is 
		 */
		http
			.csrf( csrf -> csrf
					.ignoringRequestMatchers(PathRequest.toH2Console())
					.disable()
			)
			.authorizeHttpRequests( auth -> auth
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				
			)
			.headers( headers -> headers.frameOptions(FrameOptionsConfig::disable) );
		
		return http.build();
	}
	
}
