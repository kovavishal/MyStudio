/**
 * 
 */
package com.studio.controller;

import java.io.IOException;

import javax.persistence.Entity;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author ezhilraja_k
 *
 */

@WebFilter(filterName="userFilter",urlPatterns="/*")
public class UserFilter implements Filter {

	Logger logger = Logger.getLogger(UserFilter.class);
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		Cookie[] cookies = httpServletRequest.getCookies();
		String uri = httpServletRequest.getRequestURI();
		if(!uri.equals("/MyStudio/")&& !uri.equals("/MyStudio/forgotpassword") 
				&& !uri.equals("/MyStudio/updatepassword") && !uri.equals("/MyStudio/login")
				&&!uri.contains("/css/")&&!uri.contains("/fonts/")&& !uri.contains("/images/") && !uri.contains("/js/")){
			Boolean isValidAccess=false;
			if(cookies !=null){
				for(Cookie cookie : cookies){
					if("empid".equals(cookie.getName())){
						isValidAccess=true;
					}
				}
				
			}
			if(!isValidAccess)
//				request.getRequestDispatcher("login").forward(request, response);
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
		}
		
		chain.doFilter(request, response);
		 
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
