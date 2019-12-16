package com.jacdev.picplacerest.config.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.jacdev.picplacerest.config.security.SecurityConstants.EXPIRATION_TIME;
import static com.jacdev.picplacerest.config.security.SecurityConstants.HEADER_STRING;
import static com.jacdev.picplacerest.config.security.SecurityConstants.SECRET;
import static com.jacdev.picplacerest.config.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacdev.picplacerest.model.user.UserEntity;
import com.jacdev.picplacerest.model.user.UserRole;
import com.jacdev.picplacerest.repository.user.UserRepository;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    	System.out.println("Entered JwtAuthenticationFilter()");
        this.authenticationManager = authenticationManager;
      
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserEntity user = new ObjectMapper()
                    .readValue(req.getInputStream(), UserEntity.class);
            
            System.out.println("Attempting authentication! with creds: " + user.getUsername() + " " + user.getPassword());
            System.out.println("other info");
            System.out.println("authorities :" + Arrays.toString(user.getAuthorities().toArray()));
            System.out.println("email :" + user.getEmail());
            

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    		user.getUsername(),
                    		user.getPassword(),
                            Collections.emptyList())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
    private void log(String msg) {
    	System.out.println("JwtAuthenticationFilter" + msg);
    }
    
    
    
    
    private void initUserRepository(HttpServletRequest request) {
    	
    	if(userRepository == null) {

            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            userRepository = webApplicationContext.getBean(UserRepository.class);
    	}
    	if(userRepository == null) {
    		log("User repository is still null after loading the bean!");
    	}
    	
    	
    }
    
    
    

    private List<String> getRolesListFromGrantedAuthorities(Authentication auth){

    	List <String> grantedAuthorities = new ArrayList<>();
    	for(GrantedAuthority gAuth : auth.getAuthorities()) {
    		log("GrantedAuthorites from auth: " + gAuth.getAuthority());
    		grantedAuthorities.add(gAuth.getAuthority());
    	}
    	return grantedAuthorities;
    }
    
    
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

    	
    	initUserRepository(req);
    	
    	String username = ((User) auth.getPrincipal()).getUsername();
    	JWTCreator.Builder tokenBuilder = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
    	
    	if(userRepository == null) {
    		log("UserRepository is still null!");
    	}
    	else {
    		UserEntity userEntity = userRepository.findByUsername(username);
    		for(UserRole role: userEntity.getAuthorities()) {
    			tokenBuilder = tokenBuilder.withClaim(role.getAuthority(), "true");
    		}
    	
    		
    	}
        String token = tokenBuilder.sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token); 
        System.out.println("Authentication successful!");
                
    }
}