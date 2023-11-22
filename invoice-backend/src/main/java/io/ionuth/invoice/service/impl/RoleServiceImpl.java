package io.ionuth.invoice.service.impl;

import org.springframework.stereotype.Service;

import io.ionuth.invoice.model.AppRole;
import io.ionuth.invoice.repository.RoleRepository;
import io.ionuth.invoice.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	private final RoleRepository roleRepo;
	
	// constructor injection
	public RoleServiceImpl(RoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	@Override
	public AppRole getRoleByUserEmail(String email) {
		return roleRepo.getRoleByUserEmail(email);
	}

}
