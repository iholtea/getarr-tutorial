package io.ionuth.invoice.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.ionuth.invoice.data.UserPrincipal;
import io.ionuth.invoice.model.AppRole;
import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.repository.RoleRepository;
import io.ionuth.invoice.repository.UserRepository;

/*
 * or we could implement the UserDetailsService interface in the UserServiceImpl
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	
	// constructor injections
	public CustomUserDetailsService(UserRepository userRepo, RoleRepository roleRepo) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser appUser = userRepo.getUserByEmail(email);
		if(appUser == null) {
			throw new UsernameNotFoundException("User email not found in the database");
		}
		AppRole role = roleRepo.getRoleByUserEmail(email);
		return new UserPrincipal(appUser, role.getPermissions());
	}

}
