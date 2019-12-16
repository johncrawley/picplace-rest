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
import com.jacdev.picplacerest.model.user.UserEntity;
import com.jacdev.picplacerest.model.user.UserRole;

import  com.jacdev.picplacerest.repository.user.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    
    @Autowired
    UserRepository userRepository;
	
	
    public JwtAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        System.out.println("Authorities for user " + authentication.getName() + " count: " +  authentication.getAuthorities().size());
        authentication.getAuthorities().stream().map(x -> "Found Authority ->" + x.getAuthority()).forEach(System.out::println);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    
   private void log(String msg) {
	   System.out.println("JwtAuthorizationFilter: " + msg);
   }
    
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        Set<UserRole> authorities = new HashSet<>();
        
        if (token != null) {
            // parse the token.
        	log("UsernamePasswordAuthenticationToken: Parsing the token!");
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            
            String user = decodedJWT.getSubject();
            Map<String, Claim> claimsMap = decodedJWT.getClaims();
            log("looking for claims: ");
            for(String key: claimsMap.keySet()) {
            	Claim claim = claimsMap.get(key);
            	//log("key: " + key + " claim: " + claim.asString());
            	if(claim == null) {
            		continue;
            	}
            	String claimValue = claim.asString();
            	if(claimValue != null && claimValue.equals("true")) {
            		authorities.add(new UserRole(key));
            	}
            }
            log("End of claims, authorities count = " + authorities.size());
            		
            	
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        }
        return null;
    }
    
    
    
   
    private Set<UserRole> manuallyGetAuthoritiesFromRepository(String user) {

    	Set<UserRole> authorities = new HashSet<>();
    	if (user != null) {
        	System.out.println("JwtAuthorizationFilter UsernamePasswordAuthenticationToken() user string: " + user);
        	if(userRepository == null) {
        		System.out.println("User Repository is null!");
        	}
        	else{ UserEntity userEntity = userRepository.findByUsername(user);
        		if(userEntity != null) {
        			authorities = userEntity.getAuthorities();
        		}
        	}
        }
        return authorities;
    }

}