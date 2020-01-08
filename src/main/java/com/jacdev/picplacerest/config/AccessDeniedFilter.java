package com.jacdev.picplacerest.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.NestedServletException;

public class AccessDeniedFilter extends GenericFilterBean {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) 
			throws IOException, ServletException {
	
	    try {
	        filterChain.doFilter(request, response);
	    } catch (Exception e) {
	
	        if (e instanceof NestedServletException &&
	                ((NestedServletException) e).getRootCause() instanceof AccessDeniedException) {
	
	            HttpServletRequest rq = (HttpServletRequest) request;
	            HttpServletResponse rs = (HttpServletResponse) response;
	
	            if (isAjax(rq)) {
	                rs.sendError(HttpStatus.FORBIDDEN.value());
	            } else {
	                rs.sendRedirect("/login");
	            }
	        }
	    }
	}
	
	private Boolean isAjax(HttpServletRequest request) {
		String contentType = request.getContentType();
		String requestURI = request.getRequestURI();
		
	    return contentType != null &&
	           contentType.contains("application/json") &&
	           requestURI != null &&
	           (requestURI.contains("svc") || requestURI.contains("rest"));
	    }
}