package io.ionuth.invoice.exception;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.ionuth.invoice.data.HttpMessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExceptionUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);
	
	public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		HttpMessageResponse httpMessage = new HttpMessageResponse();
		if(exception instanceof ApiException || 
				exception instanceof DisabledException || exception instanceof LockedException || 
				exception instanceof InvalidClaimException || exception instanceof BadCredentialsException || 
				exception instanceof TokenExpiredException ) {
			httpMessage.setMessage(exception.getMessage());
			writeResponse(httpMessage, response, HttpServletResponse.SC_BAD_REQUEST);
		} else {
			// some generic message
			httpMessage.setMessage("An error occurred. Please try again");
			writeResponse(httpMessage, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	public static void writeResponse(HttpMessageResponse httpMessage, HttpServletResponse response, int statusCode) {
		try {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(statusCode);
			OutputStream out = response.getOutputStream();
			// Jackson JSON mapper to create JSON object
			ObjectMapper jsonMapper = new ObjectMapper();
			jsonMapper.writeValue(out, httpMessage);
			out.flush();
		} catch(IOException ex) {
			logger.error("Error sendin HttpResponse to client: ", ex);
		}
	}
	
	
}
