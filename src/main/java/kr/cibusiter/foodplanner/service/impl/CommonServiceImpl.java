package kr.cibusiter.foodplanner.service.impl;

import kr.cibusiter.foodplanner.params.CommonMap;
import kr.cibusiter.foodplanner.service.CommonService;
import kr.cibusiter.foodplanner.service.mapper.CommonMapper;
import kr.cibusiter.foodplanner.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by whydda on 2019-09-04
*/
@Service
@Transactional
public class CommonServiceImpl implements CommonService {

	@Autowired
    private CommonMapper commonMapper;

	@Transactional(readOnly = true)
    public UserVo selectUser(String userId) {
        return commonMapper.selectUser(userId);
    }

	@Override
	public void updateUserLoginSuccess(String userId) {

	}

	@Transactional(readOnly = true)
    public boolean chkPasswd(CommonMap map){
		int chk = commonMapper.chkPasswd(map);

		if(chk > 0){
			return true;
		}else{
			return false;
		}
	}


}
