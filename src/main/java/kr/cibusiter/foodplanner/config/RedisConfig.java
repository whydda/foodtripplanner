package kr.cibusiter.foodplanner.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {
    @Value("${spring.profiles.active}")
    private String profiles;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Value("${server.session.timeout}")
    private int sessionTimeOut;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    /**
     * Type safe representation of application.properties
     */
    @Autowired
    ClusterConfigurationProperties clusterProperties;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setBlockWhenExhausted(true);

        if(profiles.equals("dev")|| profiles.equals("staging")){
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
            jedisConnectionFactory.setHostName(redisHost);
            jedisConnectionFactory.setPort(Integer.parseInt(redisPort));
            jedisConnectionFactory.afterPropertiesSet();

            return jedisConnectionFactory;
        }else{
            return new JedisConnectionFactory(new RedisClusterConfiguration(clusterProperties.getNodes()), jedisPoolConfig);
        }


    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        if(null == connectionFactory()){
            LOGGER.error("Redis template service is not available");
            return null;
        }

        template.setConnectionFactory(connectionFactory());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisOperationsSessionRepository sessionRepository() {
        RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(connectionFactory());
        sessionRepository.cleanupExpiredSessions();
//        sessionRepository.setApplicationEventPublisher(new CustomApplicationEventPublisher(sessionRepository));
        sessionRepository.setDefaultMaxInactiveInterval(sessionTimeOut);
        return sessionRepository;
    }

    //
//	@Bean
//	public RedisCacheManager redisCacheManager() {
////		Map<String, Long> expires = new HashMap<>();
////		expires.put("user:profile", 300L);
//
//
//		RedisCacheManager redisCacheManager = new
//				RedisCacheManager(redisTemplate());
////		redisCacheManager.setUsePrefix(true);
//		redisCacheManager.setDefaultExpiration(0);
////		redisCacheManager.setExpires(expires);
//
//		return redisCacheManager;
//	}
}