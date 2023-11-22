package io.ionuth.invoice.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.ionuth.invoice.model.UserVerify;

public class UserVerifyRowMapper implements RowMapper<UserVerify> {
	
	@Override
	public UserVerify mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserVerify userVerify = new UserVerify();
		userVerify.getAppUser().setUserId(rs.getLong("user_id"));
		userVerify.getAppUser().setFirstName(rs.getString("first_name"));
		userVerify.getAppUser().setLastName(rs.getString("last_name"));
		userVerify.getAppUser().setEmail(rs.getString("email"));
		userVerify.getAppUser().setAddress(rs.getString("address"));
		userVerify.getAppUser().setPhone(rs.getString("phone"));
		userVerify.getAppUser().setTitle(rs.getString("title"));
		userVerify.getAppUser().setDescription(rs.getString("description"));
		userVerify.getAppUser().setEnabled(rs.getBoolean("enabled"));
		userVerify.getAppUser().setLocked(rs.getBoolean("locked"));
		userVerify.getAppUser().setUseMfa(rs.getBoolean("use_mfa"));
		userVerify.getAppUser().setCreatedDate(rs.getDate("created_date").toLocalDate());
		userVerify.getVerifyData().setVerifyStr(rs.getString("mfa_code"));
		userVerify.getVerifyData().setVerifyDate(rs.getTimestamp("expiration_date"));
		return userVerify;
	}
	
}
