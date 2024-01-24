package io.ionuth.invoice.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class HttpMessageResponse {
	
	//private int statusCode;
    //private HttpStatus status;
    
	private String message;
    private Map<?, ?> data = new HashMap<>(); 
    
    public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<?, ?> getData() {
		return data;
	}
	
    
}
