package io.ionuth.invoice.service;

import io.ionuth.invoice.model.AppRole;

public interface RoleService {
	
	AppRole getRoleByUserEmail(String email);
	
}
