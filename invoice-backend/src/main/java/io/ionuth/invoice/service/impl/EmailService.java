package io.ionuth.invoice.service.impl;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public void sendEmail(String subject, String content, String... mailTo ) {
		System.out.println("email sent");
	}
	
}
