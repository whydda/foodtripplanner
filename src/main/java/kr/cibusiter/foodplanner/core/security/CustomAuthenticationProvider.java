package kr.cibusiter.foodplanner.core.security;

import kr.cibusiter.foodplanner.service.CommonService;
import kr.cibusiter.foodplanner.vo.SecureUserDetailsService;
import kr.cibusiter.foodplanner.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    SecureUserDetailsService userService;


    @Autowired
    CommonService commonService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userId = authentication.getName();
//        String passWd = passwdEncode.encoding(String.valueOf(authentication.getCredentials()), "");
        String passWd = "";

        UserDetails user;
        Collection<? extends GrantedAuthority> authorities;
        String message = "아이디 또는 비밀번호를 다시 확인해주십시오.";
        try {
            //패스워드 체크
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("Id",userId.toLowerCase());
            reqMap.put("PASSWD",passWd);

            //여기서는 SSO 로그인 체크이든 패스워드 체크만 하도록 한다.
            UserVo userVo = commonService.selectUser(userId);

            if(null == userVo){
                throw new UsernameNotFoundException(message);
            }

            if(userVo.getStatusCd().equals("02")){
                message = "해당계정은 사용중지처리 되었습니다.<br>관리자에게 문의하여 주시기바랍니다.";
                throw new UsernameNotFoundException(message);
            }

            user = userService.loadUserByUsername(userId.toLowerCase());
            authorities = user.getAuthorities();

        } catch (Exception e) {
            log.info(e.toString());
            throw new UsernameNotFoundException(message);
        }

        return new UsernamePasswordAuthenticationToken(user, passWd, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);

    }
}
