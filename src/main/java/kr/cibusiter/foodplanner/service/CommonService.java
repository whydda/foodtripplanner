package kr.cibusiter.foodplanner.service;

import kr.cibusiter.foodplanner.params.CommonMap;
import kr.cibusiter.foodplanner.vo.UserVo;

/**
 * Created by whydda on 2019-09-04.
 */
public interface CommonService {

	public UserVo selectUser(String userId);
	public void updateUserLoginSuccess(String userId);
	public  boolean chkPasswd(CommonMap map);

}
