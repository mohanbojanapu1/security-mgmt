package com.grit.secureid.csrfToken;

import java.security.SecureRandom;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Implementing CSRF token in security-mgmt
 * @classsname GritCsrfToken
 * @author Mohan 
 *
 */
public class GritCsrfToken {
	
	/**
	 * 
	 * To generate CSRT token use generateCsrfToken method 
	 * @param HttpServletRequest and hidden file token
	 * @return void
	 */
	public void generateCsrfToken(HttpServletRequest httpReq, String token) {
		// Check the user session for the token cache, if none is present we create one
		@SuppressWarnings("unchecked")
		Cache<String, Boolean> csrfPreventionTokenCache = (Cache<String, Boolean>)
		httpReq.getSession().getAttribute("csrfPreventionTokenCache");
		if (csrfPreventionTokenCache == null){
			csrfPreventionTokenCache = CacheBuilder.newBuilder()
					.maximumSize(5000)
					.build();
			httpReq.getSession().setAttribute("csrfPreventionTokenCache", csrfPreventionTokenCache);
		}
		// Generate the Token and store it in the users cache

		String Token = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());
				
		csrfPreventionTokenCache.put(Token, Boolean.TRUE);
		// Add the Token to the current request so it can be used
		// by the page rendered in this request
		/*System.out.println("Random token" + Token);*/
		httpReq.setAttribute("csrfPreventionToken", Token);
		
	}
	
	
	/**
	 * 
	 * To generate CSRT token use generateCsrfToken method 
	 * @param HttpServletRequest and hidden file token and timeout
	 * @return void
	 */
	
	public void generateCsrfToken(HttpServletRequest httpReq, String token, int timeout) {
		// Check the user session for the token cache, if none is present we create one
		@SuppressWarnings("unchecked")
		Cache<String, Boolean> csrfPreventionTokenCache = (Cache<String, Boolean>)
		httpReq.getSession().getAttribute("csrfPreventionTokenCache");
		if (csrfPreventionTokenCache == null){
			csrfPreventionTokenCache = CacheBuilder.newBuilder()
					.maximumSize(5000)
					.build();
			httpReq.getSession().setAttribute("csrfPreventionTokenCache", csrfPreventionTokenCache);
		}
		// Generate the Token and store it in the users cache

		String Token = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());
				
		csrfPreventionTokenCache.put(Token, Boolean.TRUE);
		// Add the Token to the current request so it can be used
		// by the page rendered in this request
		/*System.out.println("Random token" + Token);*/
		Date dateObject = new Date();
		long millsec = dateObject.getTime() + (timeout * 60000);
		Token += "+";
		Token += Long.toString(millsec);
		httpReq.setAttribute("csrfPreventionToken", Token);
		
	}
	
	/**
	 * 
	 * To validate CSRF token using  validateToken method 
	 * @param HttpServletRequest and hidden file token
	 * @return boolean flag 
	 */
	
	public boolean validateToken(HttpServletRequest httpReq, String token) {
		
		boolean flag = false;
		//InvalidateSessionDataOfController(httpReq);
		long tokenExpiryTime = 0;
		boolean timeoutFlag = false;
		if(token.contains("+"))
		{
			String[] valArray = token.split("\\+");
			token = valArray[0];
			tokenExpiryTime = Long.parseLong(valArray[1]); 
		}else {
			timeoutFlag = true;
		}
		
		Date dateObject = new Date();
		long currentMill = dateObject.getTime();
		
		// Validate that the Token is in the cache
		Cache<String, Boolean> csrfPreventionTokenCache = (Cache<String, Boolean>)
				httpReq.getSession().getAttribute("csrfPreventionTokenCache");
		
		if (csrfPreventionTokenCache != null &&
				token != null &&
				csrfPreventionTokenCache.getIfPresent(token) != null &&
				((tokenExpiryTime > currentMill) || timeoutFlag)){
			flag = true;
		}
		return flag;
	}
}
