package kr.cibusiter.foodplanner.core.security;

import kr.cibusiter.foodplanner.conponent.RedisSession;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by whydda on 2018-04-16
 */
@Slf4j
public class DuplicationLoginCheckFilter extends GenericFilterBean {

    private final SessionRegistry sessionRegistry;
    private final UserDetailsService userDetailsService;
    private final String loginProcessingUrl;

    @Autowired
    RedisSession redisSession;

    public DuplicationLoginCheckFilter(SessionRegistry sessionRegistry, UserDetailsService userDetailsService, String loginProcessingUrl) {
        this.sessionRegistry = sessionRegistry;
        this.userDetailsService = userDetailsService;
        this.loginProcessingUrl = loginProcessingUrl;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        // post 방식에 로그인 요청이 아닌 경우 작업종료
        if (!new AntPathRequestMatcher(loginProcessingUrl, "POST").matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        boolean error = false;

        try {
            String username = request.getParameter("Id");
            String password = request.getParameter("PASSWD");
            String sessionId = "";
            String sessionToken = "";
            try{
                sessionId = sessionRegistry.getSessionInformation(username).getSessionId();
                sessionToken = sessionRegistry.getSessionInformation(username).getPrincipal().toString();
            }catch (Exception e){
                //pass
            }

            if(sessionId.equals(username) && sessionToken.equals(request.getSession().getId())){
                //이미 로그인 한 사람
                error = false;
            }else{
                if(!sessionToken.equals(request.getSession().getId()) && null != redisSession.getSession(sessionToken) && String.valueOf(request.getParameter("isLogin")).equals("N")){
                    error = true;
                }else{
                    error = false;
                }
            }

            // 이미 로그인 사용자 여부
//            SessionInformationSupport sessionInformationSupport = new SessionInformationSupport(sessionRegistry);
//            if (sessionInformationSupport.userExists(username) && null == request.getSession().getAttribute("SPRING_SECURITY_CONTEXT") && String.valueOf(request.getParameter("is_login")).equals("N")) {
//                LOGGER.debug("There is already a user logged in.");
//                error = true;
//            }else{
//                error = false;
//            }
        } catch (UsernameNotFoundException e) {
            // 없는 경우 로그인 처리.
            error = false;
        }

        if (!error) {
            chain.doFilter(request, response);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", "로그인체크");
            jsonObject.put("isLogin", "Y");
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(jsonObject);
        response.getWriter().flush();
    }
}
