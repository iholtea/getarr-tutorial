package io.ionuth.invoice.repository.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
	public AppRole getRoleByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppRole getRoleByUserEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserRole(Long userId, String roleName) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
