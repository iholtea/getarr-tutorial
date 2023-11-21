package io.ionuth.invoice.data;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.ionuth.invoice.model.AppUser;

public class UserPrincipal implements UserDetails {
	
	private final AppUser appUser;
	private final String permissions;
	
	public UserPrincipal(AppUser appUser, String permissions) {
		this.appUser = appUser;
		this.permissions = permissions;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.stream(permissions.split(","))
				.map(String::trim)
				.map(SimpleGrantedAuthority::new)
				.toList();
	}

	@Override
	public String getPassword() {
		return appUser.getPassword();	
	}

	@Override
	public String getUsername() {
		return appUser.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !appUser.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return appUser.isEnabled();
	}

}
