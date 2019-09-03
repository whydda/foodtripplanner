package kr.cibusiter.foodplanner.config;

import com.shilladfs.pushportal.component.push.AWSCreateMessageWrapper;
import com.shilladfs.pushportal.component.push.AWSSqsClient;
import com.shilladfs.pushportal.core.LoginInterceptor;
import com.shilladfs.pushportal.core.params.DefaultParamsArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.servlet.Filter;
import java.util.List;

/**
 * Created by whydda on 2017-07-17.
 */

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
        ;
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        DefaultParamsArgumentResolver defaultParamsArgumentResolver = new DefaultParamsArgumentResolver();
        argumentResolvers.add(defaultParamsArgumentResolver);
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        registry.viewResolver(resolver);
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://domain2.com")
                .allowedMethods("GET", "POST", "OPTIONS");
//                .allowedHeaders("header1", "header2", "header3")
//                .exposedHeaders("header1", "header2")
//                .allowCredentials(false).maxAge(3600);

    }

    @Bean
    public AWSCreateMessageWrapper awsCreateMessageWrapper(){
        AWSCreateMessageWrapper awsCreateMessageWrapper = new AWSCreateMessageWrapper(awsSnsEndPoint);
        return awsCreateMessageWrapper;
    }
    
    @Bean
    public AWSSqsClient awsSqsClient() {
    	AWSSqsClient sqsClient = new AWSSqsClient(awsSqsEndPoint);
    	return sqsClient;
    }

}
