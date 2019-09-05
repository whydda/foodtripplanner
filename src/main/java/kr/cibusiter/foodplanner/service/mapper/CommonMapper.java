package kr.cibusiter.foodplanner.service.mapper;

import kr.cibusiter.foodplanner.vo.UserVo;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by whydda on 2019-09-04.
 */
@Repository("commonMapper")
public interface CommonMapper {
    UserVo selectUser(String userId);
    int chkPasswd(Map<String, Object> map);
}
