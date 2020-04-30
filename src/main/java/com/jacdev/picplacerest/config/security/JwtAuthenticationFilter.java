package com.jacdev.picplacerest.config.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.jacdev.picplacerest.config.security.SecurityConstants.EXPIRATION_TIME;
import static com.jacdev.picplacerest.config.security.SecurityConstants.HEADER_STRING;
import static com.jacdev.picplacerest.config.security.SecurityConstants.SECRET;
import static com.jacdev.picplacerest.config.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacdev.picplacerest.user.UserEntity;
import com.jacdev.picplacerest.user.UserRole;
import com.jacdev.picplacerest.user.repository.UserRepository;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    	System.out.println("Entered JwtAuthenticationFilter()");
        this.authenticationManager = authenticationManager;
    }
    

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException, BadCredentialsException{
        try {
            UserEntity user = new ObjectMapper().readValue(req.getInputStream(), UserEntity.class);
            logUserDetails(user);
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		user.getUsername(),
                    		user.getPassword(),
                            Collections.emptyList())
            		);
            return auth;
        } catch(IOException  e) {
        	e.printStackTrace();
        	return null;
        }
    }
    
    
    private void logUserDetails(UserEntity user) {
    	log("Attempting authentication! with creds: " + user.getUsername() + " " + user.getPassword());
    	log("authorities :" + Arrays.toString(user.getAuthorities().toArray()));
    	log("email :" + user.getEmail());
    }

    
    private void log(String msg) {
    	System.out.println("JwtAuthenticationFilter " + msg);
    }
    
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	initUserRepository(req);
    	String username = getUsername(auth);
        String token = createToken(username);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
    
    
    
    private void initUserRepository(HttpServletRequest request) { 	
    	if(userRepository != null) {
    		return;
    	}
    	ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        userRepository = webApplicationContext.getBean(UserRepository.class);
    }
    
    
    private String getUsername(Authentication auth) {
    	return ((User) auth.getPrincipal()).getUsername();
    }
    
    
    private String createToken(String username){
    	JWTCreator.Builder tokenBuilder =  JWT.create()
        .withSubject(username)
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME));	
    	
    	tokenBuilder = addClaims(tokenBuilder, username);
    	return tokenBuilder.sign(HMAC512(SECRET.getBytes()));
    }
    
    
    private JWTCreator.Builder addClaims(JWTCreator.Builder tokenBuilder, String username){
    	if(userRepository == null) {
    		return tokenBuilder;
    	}
    	UserEntity userEntity = userRepository.findByUsername(username);
    	for(UserRole role: userEntity.getAuthorities()) {
			tokenBuilder = tokenBuilder.withClaim(role.getAuthority(), "true");
		}
    	return tokenBuilder;
    }
    
    
}