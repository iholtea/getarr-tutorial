package io.ionuth.invoice.service.imp;

import org.springframework.stereotype.Service;

import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.repository.UserRepository;
import io.ionuth.invoice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	
	// constructor injection
	public UserServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public AppUser createUser(AppUser appUser) {
		return userRepo.create(appUser);
	}
	
	@Override
	public AppUser getUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}

}
