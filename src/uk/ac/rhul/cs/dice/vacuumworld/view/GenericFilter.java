package uk.ac.rhul.cs.dice.vacuumworld.view;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class GenericFilter implements Filter {
	private FilterConfig config;
	
	public FilterConfig getFilterConfig() {
		return this.config;
	}
	
	@Override
	public void destroy() {
		this.config = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
}