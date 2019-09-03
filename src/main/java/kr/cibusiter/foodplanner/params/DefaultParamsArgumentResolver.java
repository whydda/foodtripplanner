package kr.cibusiter.foodplanner.params;

/**
 * Created by whydda on 2017-07-13.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kikiwawa.kiwaweb.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by whydda on 2017-03-03.
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
                String value = URLDecoder.decode(String.valueOf(nativeWebRequest.getParameter(key)), "UTF-8");
                Object json = CommonUtils.customJSONTokener(value);
                if (json instanceof JSONObject) {
                    objectMapper.readValue(value, Map.class).forEach((k, v) ->
                            defaultParams.put((String) k, v))
                    ;
                } else if (json instanceof JSONArray) {
                    defaultParams.put(key, objectMapper.readValue(value, List.class));
                } else {
                    defaultParams.put(key, value);
                }
            }

            if(nativeWebRequest.getNativeRequest() instanceof MultipartHttpServletRequest){
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)nativeWebRequest.getNativeRequest();
                defaultParams.put("multipartHttpServletRequest", multipartHttpServletRequest);
            }

            //세션정보가 있는지 확인한다. 세션정보가 있을 경우 ROLE 이라는 param을 만든다.
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = sra.getRequest().getSession();

            if(StringUtils.equals(String.valueOf(session.getAttribute("ROLE")), "USER")){
                defaultParams.put("ROLE", session.getAttribute("ROLE"));
                defaultParams.put("WEB_ID", ((Map<String, Object>)session.getAttribute("userInfo")).get("WEB_ID"));
            }else{
                defaultParams.put("ROLE", null);
            }

            return defaultParams;
        }
        return null;
    }
}
