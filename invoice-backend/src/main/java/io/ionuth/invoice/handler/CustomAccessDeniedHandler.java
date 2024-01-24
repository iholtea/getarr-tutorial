package io.ionuth.invoice.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import io.ionuth.invoice.data.HttpMessageResponse;
import io.ionuth.invoice.exception.ExceptionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, 
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String userStr = auth != null ? "User " + auth.getName() : "User not signed in";
		logger.warn(userStr + " attempted to access the protected URL: " + request.getRequestURI());
		
		HttpMessageResponse httpMessage = new HttpMessageResponse();
		httpMessage.setMessage("Not enough permissions to access this resource");
		ExceptionUtil.writeResponse(httpMessage, response, HttpServletResponse.SC_FORBIDDEN);
		
		
	}

}
