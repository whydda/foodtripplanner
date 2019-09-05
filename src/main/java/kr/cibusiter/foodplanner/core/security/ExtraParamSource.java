package kr.cibusiter.foodplanner.core.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class ExtraParamSource implements
        AuthenticationDetailsSource<HttpServletRequest, ExtraParam> {

	public ExtraParam buildDetails(HttpServletRequest context) {
		return new ExtraParam(context);
	}
}
