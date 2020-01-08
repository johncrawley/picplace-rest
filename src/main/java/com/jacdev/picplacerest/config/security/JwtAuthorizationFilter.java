package com.jacdev.picplacerest.config.security;

import static com.jacdev.picplacerest.config.security.SecurityConstants.HEADER_STRING;
import static com.jacdev.picplacerest.config.security.SecurityConstants.SECRET;
import static com.jacdev.picplacerest.config.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jacdev.picplacerest.model.user.UserRole;

import com.jacdev.picplacerest.repository.user.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	@Autowired
	UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);

		if (nonExistantOrIncorrect(header)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		logUserAuthorities(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private boolean nonExistantOrIncorrect(String header) {
		return header == null || !header.startsWith(TOKEN_PREFIX);
	}

	private void logUserAuthorities(UsernamePasswordAuthenticationToken authentication) {
		log("Authorities for user " + authentication.getName() + " count: " + authentication.getAuthorities().size());
		authentication.getAuthorities().stream().map(x -> "Found Authority ->" + x.getAuthority())
				.forEach(System.out::println);
	}

	private void log(String msg) {
		System.out.println("JwtAuthorizationFilter: " + msg);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);

		if (token == null) {
			return null;
		}
		DecodedJWT decodedJWT = deriveDecodedTokenFrom(token);
		String user = decodedJWT.getSubject();
		Map<String, Claim> claimsMap = decodedJWT.getClaims();
		Set<UserRole> authorities = getAuthoritiesFrom(claimsMap);
		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	private DecodedJWT deriveDecodedTokenFrom(String token) {

		return JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build().verify(token.replace(TOKEN_PREFIX, ""));
	}

	private Set<UserRole> getAuthoritiesFrom(Map<String, Claim> claimsMap) {

		Set<UserRole> authorities = new HashSet<>();
		for (String key : claimsMap.keySet()) {
			Claim claim = claimsMap.get(key);
			if (claim == null) {
				continue;
			}
			String claimValue = claim.asString();
			if (claimValue != null && claimValue.equals("true")) {
				authorities.add(new UserRole(key));
			}
		}
		return authorities;
	}

}