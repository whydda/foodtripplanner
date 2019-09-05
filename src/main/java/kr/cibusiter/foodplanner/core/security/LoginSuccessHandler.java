package kr.cibusiter.foodplanner.core.security;

import kr.cibusiter.foodplanner.utils.CommonUtils;
import kr.cibusiter.foodplanner.vo.SecureUser;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 */
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private SessionRegistry sessionRegistry;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		SecureUser secureUser = (SecureUser) authentication.getPrincipal();
		//로그인 성공으로 본다.
		handle(request, response,authentication );
	}

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		SecureUser secureUser = (SecureUser) authentication.getPrincipal();

		if(String.valueOf(request.getParameter("saveId")).equals("Y")){
			CommonUtils.setCookie(response, "Id", secureUser.getId(), 60 * 60 * 24 * 365, "SAVE_ID");
		}else{
			CommonUtils.setCookie(response, "Id", "", 0, "");
		}

		JSONObject jsonObject = new JSONObject();
		try {
			sessionRegistry.registerNewSession(secureUser.getId(), request.getSession().getId());
		} catch (Exception e) {
			log.error("로그인 성공 처리 오류", e);
		}

		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().print(jsonObject);
		response.getWriter().flush();
	}
}
// :)--
