package io.ionuth.invoice.repository;

import java.util.Collection;

import io.ionuth.invoice.model.AppUser;

public interface UserRepository {
	
	// Basic CRUD operations
	AppUser create(AppUser appUser);
	Collection<AppUser> list(int page, int pageSize);
	AppUser get(Long id);
	AppUser update(AppUser appUser);
	Boolean delete(Long id);
	
	AppUser getUserByEmail(String email);
	
}
