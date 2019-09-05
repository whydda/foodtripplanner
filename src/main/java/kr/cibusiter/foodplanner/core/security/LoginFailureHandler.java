package kr.cibusiter.foodplanner.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.cibusiter.foodplanner.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 */
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Autowired
    ObjectMapper objectMapper;

	@Autowired
	CommonService commonService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

		JSONObject jsonObject = new JSONObject();
		try {
			String message = exception.getMessage();
			jsonObject.put("resultMsg", message);
			jsonObject.put("resultCode", 401);
		} catch (Exception e) {
			log.error("로그인 실패 처리 오류", e);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().print(jsonObject);
		response.getWriter().flush();
	}

}
// :)--
