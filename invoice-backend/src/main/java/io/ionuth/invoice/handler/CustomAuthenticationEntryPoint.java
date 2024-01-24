package io.ionuth.invoice.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import io.ionuth.invoice.data.HttpMessageResponse;
import io.ionuth.invoice.exception.ExceptionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		logger.warn("User that isn't signed in attempted to access the protected URL: " + request.getRequestURI());
		
		HttpMessageResponse httpMessage = new HttpMessageResponse();
		httpMessage.setMessage("You need to be logged in to access this resource");
		ExceptionUtil.writeResponse(httpMessage, response, HttpServletResponse.SC_UNAUTHORIZED);
		
	}

}
