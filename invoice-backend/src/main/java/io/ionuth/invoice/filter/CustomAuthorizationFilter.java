package io.ionuth.invoice.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.ionuth.invoice.exception.ExceptionUtil;
import io.ionuth.invoice.jwt.TokenProvider;
import io.ionuth.invoice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
	
	private static final String TOKEN_NAME_PREFIX = "Bearer ";
	
	private static final List<String> ALLOWED_URLS = Arrays.asList(
			"/h2-console",
			"/error",
			"/api/v1/users/login",
			"/api/v1/users/register",
			"/api/v1/users/verify/code");
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private final TokenProvider tokenProvider;
	
	// constructor injection
	public CustomAuthorizationFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		
		// OPTIONS is a pre-flight requests, browser asks the server some initial stuff
		boolean isOptions = request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name());
		
		String requestURI = request.getRequestURI();
		boolean isAllowedRoute = ALLOWED_URLS.stream()
				.filter( route -> requestURI.startsWith(route) )
				.findAny().isPresent();
		
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		return isAllowedRoute || isOptions || 
				authHeader == null || !authHeader.startsWith(TOKEN_NAME_PREFIX);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = getToken(request);
			String email = tokenProvider.getSubject(token, request);
			if( tokenProvider.isTokenValid(email, token) ) {
				List<? extends GrantedAuthority> authorities = tokenProvider.getAuthorities(token);
				Authentication authentication = tokenProvider.getAuthentication(
						token, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				logger.warn("Invalid token for user: {}", email);
				SecurityContextHolder.clearContext();
			}
			filterChain.doFilter(request, response);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			ExceptionUtil.processError(request, response, ex);
		}
	}
	
	private String getToken(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION).replace(TOKEN_NAME_PREFIX, "");
	}

}
