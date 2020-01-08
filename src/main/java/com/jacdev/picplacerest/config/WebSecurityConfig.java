package com.jacdev.picplacerest.config;



import static com.jacdev.picplacerest.config.security.SecurityConstants.SIGN_UP_URL;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import com.jacdev.picplacerest.config.security.JwtTokenProvider;
import com.jacdev.picplacerest.config.security.JwtAuthenticationFilter;
import com.jacdev.picplacerest.config.security.JwtAuthorizationFilter;

@EnableWebSecurity
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String USER= "USER";
	private static final String ADMIN = "ADMIN";
	
	
	@Bean
	public static BCryptPasswordEncoder BCryptPasswordEncoder(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	@Autowired
	DataSource dataSource;
	
	
	//Enable jdbc authentication
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("****** Configuring datasource for authentication manager builder!");
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(BCryptPasswordEncoder());
	}
	    
	 @Override
	 protected void configure(HttpSecurity http) throws Exception {
	       	
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager());
		authenticationFilter.setFilterProcessesUrl("/svc/login");
				 
	        http
	            .httpBasic().disable()
	            .csrf().disable()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and()
	                .authorizeRequests()
	                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
	                .antMatchers(HttpMethod.GET, "/svc/test1").permitAll()
	                .antMatchers(HttpMethod.GET, "/svc/login").permitAll()
	                .antMatchers(HttpMethod.GET, "/svc/uploadFile").permitAll()
	                .antMatchers(HttpMethod.GET, "/svc/test2").hasAuthority(USER)
	                .antMatchers(HttpMethod.GET, "/svc/test3").hasAuthority(USER)
	            .and()
	            .addFilter(authenticationFilter)
	            .addFilter(new JwtAuthorizationFilter(authenticationManager()));
	    } 
		
}


