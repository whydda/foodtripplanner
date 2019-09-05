package kr.cibusiter.foodplanner.vo;

import kr.cibusiter.foodplanner.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자정보를 가지고 온다.
 */
@Service(value = "secureUserDetailsService")
public class SecureUserDetailsService implements UserDetailsService {

	private CommonService commonService;

	@Autowired
	public SecureUserDetailsService(CommonService commonService){
		this.commonService = commonService;
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserDetails userDetails = getUser(userId);
		return userDetails;
	}

	private SecureUser getUser(String userId) {
		//DB호출 후 계정정보 리턴
		UserVo userVo = commonService.selectUser(userId);
		SecureUser user;

		if (userVo.getAuthCd().equals("01")) {
			user = new SecureUser(userVo, AuthorityUtils.createAuthorityList("ROLE_GUEST"));
		} else if (userVo.getAuthCd().equals("02")) {
			user = new SecureUser(userVo, AuthorityUtils.createAuthorityList("ROLE_USER"));
		} else {
			user = new SecureUser(userVo, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		}

		commonService.updateUserLoginSuccess(userVo.getId());

		return user;
	}
}
