package kr.cibusiter.foodplanner.params;

/**
 * Created by whydda on 2017-07-13.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhncorp.lucy.security.xss.XssPreventer;
import kr.cibusiter.foodplanner.service.CommonService;
import kr.cibusiter.foodplanner.vo.SecureUser;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: whydda
 * Date: 2019-09-04
 * Time: 오후 4:55
 */
public class DefaultParamsArgumentResolver implements HandlerMethodArgumentResolver {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return DefaultParams.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        if(methodParameter.getParameterType().equals(DefaultParams.class)) {
            DefaultParams defaultParams = new DefaultParams();
            defaultParams.setMap(new LinkedHashMap());
            Iterator iterator = nativeWebRequest.getParameterNames();

            while(iterator.hasNext()) {
                String key = (String)iterator.next();
                String value = nativeWebRequest.getParameter(key);

                if(!StringUtils.isEmpty(value) && !StringUtils.equals(value.toLowerCase(), "null") && !StringUtils.isEmpty(value.trim())){
                    Object json = new JSONTokener(value).nextValue();
                    if(json instanceof JSONObject){
                        objectMapper.readValue(value, Map.class).forEach((k, v) -> {
                            if(v instanceof String){
                                v = XssPreventer.escape(String.valueOf(v));
                                defaultParams.put((String) k, v);
                            }else{
                                defaultParams.put((String) k, v);
                            }
                        })
                        ;
                    }else if (json instanceof JSONArray){
                        defaultParams.put(key, objectMapper.readValue(value, List.class));
                    } else {
                        defaultParams.put(key, XssPreventer.escape(String.valueOf(value)));
                    }
                }else{
                    if(StringUtils.equals("null", value) == true){
                        value = "";
                    }
                    defaultParams.put(key, XssPreventer.escape(String.valueOf(value)));
                }
            }

            if(!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                SecureUser secureUser = ((SecureUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSecureUser();

                //로그를 쌓는다.
                HttpServletRequest request = (HttpServletRequest)nativeWebRequest.getNativeRequest();

                //HttpSession 객체 가져오기
                HttpSession session = request.getSession();

                //ServletContext 객체 가져오기
                ServletContext conext = session.getServletContext();

                //Spring Context 가져오기
                WebApplicationContext wContext = WebApplicationContextUtils.getWebApplicationContext(conext);

                String tempIp = request.getHeader("X-FORWARDED-FOR");
                String ip = "";
                if (tempIp == null) {
                    ip = request.getRemoteAddr();
                }else{
                    String[] ipArr = tempIp.replaceAll("\\s", "").split(",");
                    ip = ipArr[0];
                }

                String json = objectMapper.writeValueAsString(defaultParams.getMap());

                if (!String.valueOf(secureUser.getId()).equals("null")) {
                    defaultParams.put("Id", secureUser.getId());
                    defaultParams.put("AUTH_CD", secureUser.getAuthCd());
                    defaultParams.put("USER_NAME", secureUser.getUserNm());
                }
            }

            if(nativeWebRequest.getNativeRequest() instanceof MultipartHttpServletRequest){
                List<Map<String, Object>> uploadFileMap = new ArrayList<>();
                MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest)nativeWebRequest.getNativeRequest();
                for(String name : mpReq.getFileMap().keySet()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("key", name);
                    //new String(String.valueOf(mpReq.getFiles(name)).getBytes("8859_1"),"utf-8")
                    map.put("files", mpReq.getFiles(name));
                    uploadFileMap.add(map);
                }
                defaultParams.put("uploadFileMap", uploadFileMap);
            }

            return defaultParams;
        }
        return null;
    }


}
