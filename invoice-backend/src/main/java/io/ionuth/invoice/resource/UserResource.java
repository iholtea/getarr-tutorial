package io.ionuth.invoice.resource;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.ionuth.invoice.data.LoginForm;
import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserResource {
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	
	// constructor injection
	public UserResource(UserService userService,
			AuthenticationManager authenticationManager) {
		
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}
	
	@PostMapping("/login")
	public ResponseEntity<AppUser> login(@RequestBody @Valid LoginForm loginForm) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				loginForm.getEmail(), loginForm.getPassword());
		// this will throw AuthenticationException if authentication fails
		authenticationManager.authenticate(authentication);
		AppUser appUser = userService.getUserByEmail(loginForm.getEmail());
		return ResponseEntity.ok(appUser);
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> loginGet() {
		return ResponseEntity.ok("get works");
	}
	
	@PostMapping("/register")
	public ResponseEntity<AppUser> saveUser(@RequestBody @Valid AppUser appUser) {
		userService.createUser(appUser);
		String relPath = "/api/v1/users/get/" + appUser.getUserId();
		String fullPath = ServletUriComponentsBuilder.fromCurrentContextPath().path(relPath).toUriString();
		return ResponseEntity.created(URI.create(fullPath)).body(appUser);
	}
	
}
