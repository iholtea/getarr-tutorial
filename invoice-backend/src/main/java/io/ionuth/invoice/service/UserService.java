package io.ionuth.invoice.service;

import io.ionuth.invoice.model.AppUser;

public interface UserService {
	
	AppUser createUser(AppUser appUser);
	
	AppUser getUserByEmail(String email);
	
	void sendVerificationCode(AppUser user);
}
