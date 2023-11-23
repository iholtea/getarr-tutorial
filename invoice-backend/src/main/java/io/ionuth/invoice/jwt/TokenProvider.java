package io.ionuth.invoice.jwt;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import io.ionuth.invoice.data.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;

/*
 *  issuer : who issues the token - company or application name for example
 *  audience : recipients that the token is intended for 
 */

@Component
public class TokenProvider {
	
	// 30 minutes
	private static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000;
	// 5 days
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 5 * 24 * 3600000;
	
	private static final String AUTHORITIES = "authorities";
	
	private Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	
	@Value("${jwt.secret}")
	private String secret;
	
	public String createAccessToken(UserPrincipal userPrincipal) {
		String[] claims = getClaimsFromUser(userPrincipal);
		long currentMillis = System.currentTimeMillis();
		return JWT.create()
				//.withIssuer("my company")
				//.withAudience("customer management department")
				.withSubject(userPrincipal.getUsername())
				.withArrayClaim(AUTHORITIES, claims)
				.withIssuedAt(new Date(currentMillis))
				.withExpiresAt(new Date(currentMillis + ACCESS_TOKEN_EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret));
	}
	
	public String createRefreshToken(UserPrincipal userPrincipal) {
		long currentMillis = System.currentTimeMillis();
		return JWT.create()
				//.withIssuer("my company")
				//.withAudience("customer management department")
				.withSubject(userPrincipal.getUsername())
				.withIssuedAt(new Date(currentMillis))
				.withExpiresAt(new Date(currentMillis + REFRESH_TOKEN_EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret));
	}

	private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
		return userPrincipal.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toArray(String[]::new);
	}
	
	private String[] getClaimsFromToken(String authToken) {
		JWTVerifier verifier = getJWTVerfier();
		return verifier.verify(authToken).getClaim(AUTHORITIES).asArray(String.class);
	}
	
	public List<? extends GrantedAuthority> getAuthorities(String authToken) {
		String[] claims = getClaimsFromToken(authToken);
		return Arrays.stream(claims).map(SimpleGrantedAuthority::new).toList();
	}
	
	private JWTVerifier getJWTVerfier() {
		JWTVerifier verifier;
		Algorithm algorithm = Algorithm.HMAC512(secret);
		verifier = JWT.require(algorithm).build();
		return verifier;
	}

	public Authentication getAuthentication(String email, 
				List<? extends GrantedAuthority> authorities,
				HttpServletRequest request) {
		
		UsernamePasswordAuthenticationToken userPassAuth = new UsernamePasswordAuthenticationToken(
				email, null, authorities);
		userPassAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return userPassAuth;
	}
	
	public boolean isTokenValid(String email, String token) {
		return StringUtils.isNotEmpty(email) && !isTokenExpired(token);
	}
	
	public boolean isTokenExpired(String token) {
		JWTVerifier verifier = getJWTVerfier();
		Date expirationDate = verifier.verify(token).getExpiresAt();
		return expirationDate.before(new Date()); 
	}
	
	public String getSubject(String token, HttpServletRequest request) {
		JWTVerifier verifier = getJWTVerfier();
		try {
			return verifier.verify(token).getSubject();
		} catch(TokenExpiredException ex) {
			request.setAttribute("expiredMessage", ex.getMessage());
		} catch(InvalidClaimException ex) {
			request.setAttribute("invalidClaim", ex.getMessage());
		} 
		return null;
	}
}
