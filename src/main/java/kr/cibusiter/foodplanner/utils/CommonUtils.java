package kr.cibusiter.foodplanner.utils;

import kr.cibusiter.foodplanner.params.CommonMap;
import kr.cibusiter.foodplanner.utils.excel.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by whydda on 2017-07-24.
 */
public class CommonUtils {

    /**
     * 쿠키값 저장
     * @param res
     * @param strCookieName
     * @param strValue
     * @param nMaxAge
     * @param strComment
     */
    public static void setCookie(HttpServletResponse res
            , String strCookieName
            , String strValue
            , int nMaxAge
            , String strComment) {

        Cookie cookie = new Cookie(strCookieName, strValue);
        cookie.setVersion(0);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(nMaxAge);
        cookie.setComment(strComment);
        res.addCookie(cookie);
    }

    /**
     * 쿠키값 불러오기
     * @param req
     * @param strCookieName
     * @return
     */
    public static String getCookie(HttpServletRequest req
            , String strCookieName) {
        Cookie cookies[] = req.getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; ++i) {
                if (cookies[i].getName().equals(strCookieName)) {
                    cookie = cookies[i];
                    break;
                }
            }
        }
        String strValue = "";
        try {
            strValue = cookie.getValue();
        } catch (Exception e) {}
        return strValue;
    }

    /**
     * 값이 없는 Object 객체 삭제
     * @param i
     * @return
     */
    public static boolean removeObjectNull(Map<String, String> i) {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(false);

        i.forEach((k, v) -> {
            if(StringUtils.isEmpty(v)){
                atomicBoolean.set(true);
            }
        });

        return atomicBoolean.get();
    }


    /**
     * 키배열 ROWNUM에 맞게 object 재정렬
     * @param
     * @return
     */
    public static List<Map<String, Object>> sortForKeyRealignment(List<Map<String, Object>> list , String[] arrKey) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        Map<String,Object> tempMap = new LinkedHashMap();

        for (Map<String, Object> standRt : list) {
            for(String sortKey : arrKey) {
                Iterator<String> keys = standRt.keySet().iterator();
                while(keys.hasNext()) {
                    String key = keys.next();
                    if(sortKey.equals(key) && !sortKey.equals("ROWNUM")) {
                        tempMap.put(sortKey, standRt.get(key));
                    }
                }
            }
            tempList.add(tempMap);
            tempMap = new LinkedHashMap();
        }

        return tempList;
    }

    /**
     * 월, 일이 한자린지 체크한다.
     * @param mm
     * @return
     */
    public static String addZero(String mm){
        String tempMm = mm;
        if(tempMm.length() > 1){
            return tempMm;
        }else{
            return "0" + tempMm;
        }
    }

    /**
     * 패스워드 생성
     * @param size
     * @return
     */
    public static String makePassword(int size){

        StringBuffer sb = new StringBuffer();
        int cycle = 2;
        Random random = new Random();
        while((cycle++)<=size){
            if(cycle % 3 == 0){
                sb.append((char)((Math.random() * 10) + 48)); //숫자 생성
            }else if(cycle % 2 == 0){
                sb.append((char)((Math.random() * 26) + 97)); //소문자 생성
            }else{
                sb.append((char)((Math.random() * 26) + 65)); //대문자 생성
            }
        }

        int length = random.nextInt(size);
        sb.insert(length, "!");
        length = random.nextInt(size);
        sb.insert(length, "@");

        return sb.toString();
    }

    /**
     * jquery.fileDownload.js 를 이용한 엑셀다운로드
     * @param excelList
     * @param file_name
     * @param response
     * @throws Exception
     */
    public static void excelDataDownload(List<CommonMap> excelList, String file_name, HttpServletResponse response) throws Exception {

        if (excelList.size() == 0) {

            throw new Exception("데이터의 갯수가 0개입니다.");
        } else {

            String colNames = "";
            Map<String, Object> firstRow = excelList.get(0);
            Iterator<String> keys = firstRow.keySet().iterator();
            while(keys.hasNext()) {
                colNames += keys.next();
                if (keys.hasNext()) {
                    colNames += ",";
                }
            }
            HSSFWorkbook book = ExcelUtils.excelDataDownloadXls(excelList, colNames, file_name);
            ExcelUtils.write(response, book, file_name, true);
        }
    }
}
