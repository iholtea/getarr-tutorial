package io.ionuth.invoice.resource;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.ionuth.invoice.data.LoginForm;
import io.ionuth.invoice.data.UserPrincipal;
import io.ionuth.invoice.jwt.TokenProvider;
import io.ionuth.invoice.model.AppRole;
import io.ionuth.invoice.model.AppUser;
import io.ionuth.invoice.service.RoleService;
import io.ionuth.invoice.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserResource {
	
	private final UserService userService;
	private final RoleService roleService;
	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	
	// constructor injection
	public UserResource(UserService userService,
			RoleService roleService,
			AuthenticationManager authenticationManager,
			TokenProvider tokenProvider) {
		
		this.userService = userService;
		this.roleService = roleService;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginForm loginForm) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				loginForm.getEmail(), loginForm.getPassword());
		// this will throw AuthenticationException if authentication fails
		authenticationManager.authenticate(authentication);
		AppUser appUser = userService.getUserByEmail(loginForm.getEmail());
		if(appUser.isUseMfa()) {
			return sendVerificationCode(appUser);
		} else {
			return sendResponse(appUser);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<AppUser> saveUser(@RequestBody @Valid AppUser appUser) {
		userService.createUser(appUser);
		String relPath = "/api/v1/users/get/" + appUser.getUserId();
		String fullPath = ServletUriComponentsBuilder.fromCurrentContextPath().path(relPath).toUriString();
		return ResponseEntity.created(URI.create(fullPath)).body(appUser);
	}
	
	// TODO is is safe to send it in the URL ? is this encrypted with https ?
	// or we should sent it as post ?
	@GetMapping("/verify/code/{email}/{code}")
	public ResponseEntity<?> verifyCode( @PathVariable("email") String email,
			@PathVariable("code") String code) {
		
		AppUser appUser = userService.verifyCode(email, code);
		// TODO check exceptions or verify against null
		if( appUser != null ) {
			return sendResponse(appUser);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Code could not be verified. Please try again");
		}
	}
	
	
	private ResponseEntity<?> sendResponse(AppUser appUser) {
		UserPrincipal userPrincipal = getUserPrincipal(appUser);
		Map<String, Object> data = Map.of(
				"access_token", tokenProvider.createAccessToken(userPrincipal), 
				"refresh_token", tokenProvider.createRefreshToken(userPrincipal),
				"user", appUser
				); 
		return ResponseEntity.ok(data);
	}

	private ResponseEntity<?> sendVerificationCode(AppUser appUser) {
		String verificationCode = userService.sendVerificationCode(appUser);
		return ResponseEntity.ok("verification code sent: " + verificationCode);
	}
	
	private UserPrincipal getUserPrincipal(AppUser appUser) {
		AppRole role = roleService.getRoleByUserEmail(appUser.getEmail());
		return new UserPrincipal(appUser, role.getPermissions());
	}
}
