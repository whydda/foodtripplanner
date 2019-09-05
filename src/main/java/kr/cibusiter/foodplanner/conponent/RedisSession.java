package kr.cibusiter.foodplanner.conponent;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisSession {

	@Resource(name="redisTemplate")
	private ValueOperations<String, String> tokens;

	public String getSession(String sessionId){
		return tokens.get(sessionId);
	}

}
