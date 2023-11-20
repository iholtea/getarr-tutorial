package io.ionuth.invoice.resource;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserResource {
	
	private final UserService userService;
	
	// constructor injection
	public UserResource(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AppUser> saveUser(@RequestBody @Valid AppUser appUser) {
		userService.createUser(appUser);
		String relPath = "/api/v1/users/get/" + appUser.getUserId();
		String fullPath = ServletUriComponentsBuilder.fromCurrentContextPath().path(relPath).toUriString();
		return ResponseEntity.created(URI.create(fullPath)).body(appUser);
	}
	
}
