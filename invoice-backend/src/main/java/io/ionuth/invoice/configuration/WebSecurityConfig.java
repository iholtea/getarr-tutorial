package io.ionuth.invoice.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.ionuth.invoice.handler.CustomAccessDeniedHandler;
import io.ionuth.invoice.handler.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	private static final RequestMatcher[] ALLOWED_URLS = {
			PathRequest.toH2Console(),
			AntPathRequestMatcher.antMatcher("/api/v1/users/login"),
			AntPathRequestMatcher.antMatcher("/api/v1/test/login")
	};
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		//PathRequest.toH2Console() is equivalent to new AntPathRequestMatcher("/h2-console/**") 
		
		http.csrf( csrf -> 
			//csrf.ignoringRequestMatchers(PathRequest.toH2Console()).disable()
			csrf.disable() 
		);
		
		http.sessionManagement( session -> 
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
		);
		
		http.authorizeHttpRequests( auth -> {
			auth.requestMatchers(ALLOWED_URLS).permitAll();
			auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/user/**"))
					.hasAnyAuthority("DELETE:USER");
			auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/customer/**"))
					.hasAnyAuthority("DELETE:CUSTOMER");
			// is this still necessary or is the default ?
			auth.anyRequest().authenticated();
		});
		
		// needed for h2-console. TODO - research why is this necessary
		http.headers( headers -> headers.frameOptions(FrameOptionsConfig::disable) );
		
		// register custom handler not authenticated ( not signed in )
		// and for access denied - not authorized ( not enough permissions for signed in user )
		http.exceptionHandling( exHandle -> 
			exHandle.authenticationEntryPoint(authenticationEntryPoint())
					.accessDeniedHandler(accessDeniedHandler())
		);
		
		return http.build();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passEncoder());
		authProvider.setUserDetailsService(null);
		return new ProviderManager(authProvider);
	}
	
	@Bean
	public BCryptPasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler( ) {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	
}
