package kr.cibusiter.foodplanner.core.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class ExtraParam extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	private final String saveId;

	public ExtraParam(HttpServletRequest request) {
		super(request);
		this.saveId = request.getParameter("saveId");
	}

	public String getSaveId() {
		return saveId;
	}
}
