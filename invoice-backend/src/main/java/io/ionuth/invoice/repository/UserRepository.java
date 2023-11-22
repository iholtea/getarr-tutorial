package io.ionuth.invoice.repository;

import java.util.Collection;

import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.model.UserVerify;
import io.ionuth.invoice.model.VerifyData;

public interface UserRepository {
	
	// Basic CRUD operations
	AppUser create(AppUser appUser);
	Collection<AppUser> list(int page, int pageSize);
	AppUser get(Long id);
	AppUser update(AppUser appUser);
	Boolean delete(Long id);
	
	AppUser getUserByEmail(String email);
	
	void addVerifyMfaCode(VerifyData verifyData);
	
	UserVerify verifyMfaCode(String email, String code);
	
	void deteleMfaCodeByUserId(long userId);
	
	
}
