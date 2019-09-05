package kr.cibusiter.foodplanner.core.security;

import com.nhncorp.lucy.security.xss.XssFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;

public class RequestWrapperForXssFiltering extends HttpServletRequestWrapper {

	public RequestWrapperForXssFiltering(HttpServletRequest httpServletRequest) {
		super(httpServletRequest);
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if(values == null) return null;
		return Arrays.stream(values).map(v -> doFilter(v)).toArray(String[]::new);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if(value == null) return null;
		return doFilter(value);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if(value == null) return null;
		return doFilter(value);
	}

	public String doFilter(String value) {
		XssFilter xssFilter = XssFilter.getInstance("lucy-xss-sax.xml");
		return xssFilter.doFilter(value);
	}
}
