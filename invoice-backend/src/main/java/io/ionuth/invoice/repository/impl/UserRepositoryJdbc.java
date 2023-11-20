package io.ionuth.invoice.repository.impl;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.ionuth.invoice.exception.ApiException;
import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.model.RoleType;
import io.ionuth.invoice.model.VerifyType;
import io.ionuth.invoice.repository.RoleRepository;
import io.ionuth.invoice.repository.UserRepository;

@Repository
public class UserRepositoryJdbc implements UserRepository {
	
	private static final String INSERT_USER_QUERY = """
			INSERT INTO invoice.app_user (first_name, last_name, email, password)
			VALUES (:firstName, :lastName, :email, :password)
			""";
	
	private static final String COUNT_EMAIL_QUERY = """
			SELECT COUNT(*) FROM invoice.app_user WHERE email = :email
			""";
	
	private static final String INSERT_VERIFY_ACCOUNT_QUERY = """
			INSERT INTO invoice.verify_acct (user_id, verify_url)
			VALUES (:userId, :verifyUrl)
			""";
	
	private final NamedParameterJdbcTemplate jdbcTmpl;
	
	private final RoleRepository roleRepo;
	
	private final BCryptPasswordEncoder passEncoder;
	
	// constructor injection
	public UserRepositoryJdbc(NamedParameterJdbcTemplate jdbcTmpl, 
			RoleRepository roleRepo, 
			BCryptPasswordEncoder passEncoder) {
		
		this.jdbcTmpl = jdbcTmpl;
		this.roleRepo = roleRepo;
		this.passEncoder = passEncoder;
	}
	
	// TODO - this should be moved to the AppUserService class
	// and the Repository should handle only the operations with the database
	@Override
	public AppUser create(AppUser appUser) {
		
		if( emailExists(appUser.getEmail().trim().toLowerCase()) ) {
			throw new ApiException("Email aldready exists");
		}
		
		try {
			
			// insert user and get generated Id
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String[] keyNames = new String[] { "user_id" };
			SqlParameterSource params = getSqlParameterSource(appUser);
			jdbcTmpl.update(INSERT_USER_QUERY, params, keyHolder, keyNames);
			appUser.setUserId( keyHolder.getKey().longValue() );
			// TODO maybe these two should be in the AppUserService also
			appUser.setEnabled(false);
			appUser.setLocked(false);
			
			// add a default role to the user
			roleRepo.addRoleToUser(appUser.getUserId(), RoleType.ROLE_USER.name());
			
			// generate new account verification URL
			// add it to the DataBase and send verify account email
			String verifyUrl = generateVerifyUrl(VerifyType.ACCOUNT);
			jdbcTmpl.update(INSERT_VERIFY_ACCOUNT_QUERY, 
					Map.of("userId", appUser.getUserId(), "verifyUrl", verifyUrl));
			// TODO uncomment send mail
			// emailService.sendVerifyUrl(appUser, verifyUrl, VerifyType.ACCOUNT);
			
			return appUser;
			
		} catch(Exception ex) {
			System.out.println(ex);
			throw new ApiException("Error when creating application user");
		}
		
	}

	

	@Override
	public Collection<AppUser> list(int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppUser get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppUser update(AppUser appUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/////////  other operations //////////////
	
	public boolean emailExists(String email) {
		Integer count = jdbcTmpl.queryForObject(COUNT_EMAIL_QUERY, 
				Map.of("email", email), 
				Integer.class);
		return count > 0;
	}
	
	private SqlParameterSource getSqlParameterSource(AppUser appUser) {
		return new MapSqlParameterSource()
				.addValue("firstName", appUser.getFirstName())
				.addValue("lastName", appUser.getLastName())
				.addValue("email", appUser.getEmail())
				.addValue("password", passEncoder.encode(appUser.getPassword()));
	}
	
	private String generateVerifyUrl(VerifyType verifyType) {
		String relativePathStr = "/user/verify/" + 
				verifyType.name().toLowerCase() + "/" + 
				UUID.randomUUID().toString();
		return ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(relativePathStr).toUriString();
		 
		
	}
	
}