package io.ionuth.invoice.repository.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import io.ionuth.invoice.exception.ApiException;
import io.ionuth.invoice.mapper.RoleRowMapper;
import io.ionuth.invoice.model.AppRole;
import io.ionuth.invoice.repository.RoleRepository;

@Repository
public class RoleRepositoryJdbc implements RoleRepository {
	
	private final static String SELECT_ROLE_BY_NAME_QUERY = """
			SELECT * FROM invoice.app_role WHERE role_name = :roleName
			""";
	
	private final static String INSERT_ROLE_TO_USER_QUERY = """
			INSERT INTO invoice.user_role (user_id, role_id) VALUES (:userId, :roleId)
			""";
	
	private final static String SELECT_ROLE_BY_USER_EMAIL_QUERY = """
			SELECT r.role_id, r.role_name, r.permissions, u.email 
			FROM invoice.app_role r
			INNER JOIN invoice.user_role ur ON r.role_id = ur.role_id 
			INNER JOIN invoice.app_user u ON ur.user_id = u.user_id
			WHERE u.email = :email
			""";
	
	private final NamedParameterJdbcTemplate jdbcTmpl;
	
	// constructor injection
	public RoleRepositoryJdbc( NamedParameterJdbcTemplate jdbcTmpl ) {
		this.jdbcTmpl = jdbcTmpl;
	}
	
	@Override
	public AppRole create(AppRole appRole) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<AppRole> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppRole get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppRole update(AppRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRoleToUser(Long userId, String roleName) {
		try {
			AppRole role = jdbcTmpl.queryForObject(SELECT_ROLE_BY_NAME_QUERY, 
					Map.of("roleName", roleName), new RoleRowMapper());
			jdbcTmpl.update(INSERT_ROLE_TO_USER_QUERY, 
					Map.of("userId", userId, "roleId", role.getRoleId()));
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}

	@Override
	public AppRole getRoleByUserEmail(String email) {
		try {
			return jdbcTmpl.queryForObject(SELECT_ROLE_BY_USER_EMAIL_QUERY, 
					Map.of("email", email), new RoleRowMapper());
		} catch(EmptyResultDataAccessException ex) {
			throw new ApiException("No role found for user with email: " + email);
		} catch(Exception ex) {
			//TODO log error
			throw new ApiException("An error has occurred. Please try again.");
		}
	}

	@Override
	public void updateUserRole(Long userId, String roleName) {
		// TODO Auto-generated method stub
	}
	
	
}
