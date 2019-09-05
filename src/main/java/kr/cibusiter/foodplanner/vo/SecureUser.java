package kr.cibusiter.foodplanner.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

/**
 * SecureUser vo
 */
@Getter
@Setter
public class SecureUser extends User {
	private static final long serialVersionUID = 2550249307461767130L;

	private String Id = "";
	private String pass = "";
	private String authCd = "";
	private String userNm = "";

	public SecureUser(UserVo userVo, Collection<? extends GrantedAuthority> authorities) {
		super(userVo.getId(), userVo.getPass(), authorities);
		this.setId(userVo.getId());
		this.setPass(userVo.getPass());
		this.setAuthCd(userVo.getAuthCd());
		this.setUserNm(userVo.getUserNm());
	}

	public SecureUser getSecureUser(){
		return this;
	}
}
