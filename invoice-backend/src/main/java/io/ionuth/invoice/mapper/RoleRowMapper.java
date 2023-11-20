package io.ionuth.invoice.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.ionuth.invoice.model.AppRole;

public class RoleRowMapper implements RowMapper<AppRole> {

	@Override
	public AppRole mapRow(ResultSet rs, int rowNum) throws SQLException {
		AppRole role = new AppRole();
		role.setRoleId(rs.getLong("role_id"));
		role.setRoleName(rs.getString("role_name"));
		role.setPermissions(rs.getString("permissions"));
		return role;
	}

}
