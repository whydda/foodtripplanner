package kr.cibusiter.foodplanner.params;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by whydda on 2017-07-14.
 */

public class ResponseMap{
    private Map<String, Object> resMap;

    /**
     * 200
     * @return
     */
    public Map<String, Object> ok(String message, Object dataMap) throws Exception{
        this.resMap = new HashMap<>();
        this.resMap.put("resultCode", HttpStatus.OK.value());
        if(StringUtils.isEmpty(message)){
            this.resMap.put("resultMsg", "정상처리 되었습니다.");
        }else{
            this.resMap.put("resultMsg", message);
        }

        if(dataMap != null){
            this.resMap.put("resultData", dataMap);
        }

        return this.responseMap();
    }

    /**
     * 401
     * @return
     */
    public Map<String, Object> unauthorized(String message, Map<String, Object> dataMap) throws Exception{
        this.resMap = new HashMap<>();
        this.resMap.put("resultCode", HttpStatus.UNAUTHORIZED.value());
        if(StringUtils.isEmpty(message)){
            this.resMap.put("resultMsg", "잘못된 경로로 접근하였습니다.");
        }else{
            this.resMap.put("resultMsg", message);
        }

        if(dataMap != null){
            this.resMap.put("resultData", dataMap);
        }

        return this.responseMap();
    }

    /**
     * 500
     * @return
     */
    public Map<String, Object> internalServerError(String message, Map<String, Object> dataMap) throws Exception{
        this.resMap = new HashMap<>();
        this.resMap.put("resultCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        if(StringUtils.isEmpty(message)){
            this.resMap.put("resultMsg", "서버에서 에러가 발생하였습니다.\r\n관리자에게 문의하여 주십시오.");
        }else{
            this.resMap.put("resultMsg", message);
        }

        if(dataMap != null){
            this.resMap.put("resultData", dataMap);
        }

        return this.responseMap();
    }

    private Map<String, Object> responseMap() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.resMap);
        return objectMapper.readValue(json, HashMap.class);
    }
}
