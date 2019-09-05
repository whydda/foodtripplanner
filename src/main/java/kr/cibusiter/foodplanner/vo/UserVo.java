package kr.cibusiter.foodplanner.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by whydda on 2019-09-04.
 */
@Getter
@Setter
public class UserVo {
    private String Id;
    private String userNm;
    private String pass;
    private String authCd; //권한
    private String remark;
    private String statusCd; //상태코드
    private String lastLoginDt;
    private String useYn;
    private String regId;
    private String regDate;
    private String upId;
    private String upDate;
    private String delId;
    private String delDate;
}
