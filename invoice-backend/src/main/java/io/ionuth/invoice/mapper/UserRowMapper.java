package io.ionuth.invoice.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.ionuth.invoice.model.AppUser;

public class UserRowMapper implements RowMapper<AppUser> {

	@Override
	public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		AppUser appUser = new AppUser();
		appUser.setUserId(rs.getLong("user_id"));
		appUser.setFirstName(rs.getString("first_name"));
		appUser.setLastName(rs.getString("last_name"));
		appUser.setEmail(rs.getString("email"));
		appUser.setPassword(rs.getString("password"));
		appUser.setAddress(rs.getString("address"));
		appUser.setPhone(rs.getString("phone"));
		appUser.setTitle(rs.getString("title"));
		appUser.setDescription(rs.getString("description"));
		appUser.setEnabled(rs.getBoolean("enabled"));
		appUser.setLocked(rs.getBoolean("locked"));
		appUser.setUseMfa(rs.getBoolean("use_mfa"));
		appUser.setCreatedDate(rs.getDate("created_date").toLocalDate());
		return appUser;
	}

}
