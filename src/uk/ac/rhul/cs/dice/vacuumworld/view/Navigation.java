package uk.ac.rhul.cs.dice.vacuumworld.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * 
 * 
 * WARNING: obsolete class
 * 
 * 
 * 
 */
public class Navigation {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public Navigation(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}
}