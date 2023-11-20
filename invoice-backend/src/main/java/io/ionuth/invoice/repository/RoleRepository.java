package io.ionuth.invoice.repository;

import java.util.Collection;

import io.ionuth.invoice.model.AppRole;

public interface RoleRepository {
	
	AppRole create(AppRole appRole);
	Collection<AppRole> list();
	AppRole get(Long id);
	AppRole update(AppRole role);
	Boolean delete(Long id);
	
	void addRoleToUser(Long userId, String roleName);
	AppRole getRoleByUserId(Long userId);
	AppRole getRoleByUserEmail(String email);
	void updateUserRole(Long userId, String roleName);
}
