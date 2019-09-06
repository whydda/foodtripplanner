package kr.cibusiter.foodplanner.core.security;

import kr.cibusiter.foodplanner.conponent.RedisSession;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
    }
}
