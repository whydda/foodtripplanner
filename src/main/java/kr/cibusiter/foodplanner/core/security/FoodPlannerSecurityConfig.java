package kr.cibusiter.foodplanner.core.security;

import kr.cibusiter.foodplanner.vo.SecureUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;


/**
 * whydda
 */
@Configuration
@EnableWebSecurity
public class FoodPlannerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecureUserDetailsService secureUserDetailsService;

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    DataSource dataSource;

    @Autowired
    SessionRegistryImpl sessionRegistry;

    /**
     * security 기본 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).accessDeniedPage("/errors/error")
                .and()
                .authorizeRequests()
                .antMatchers("/favicon.ico", "/css/**", "/js/**", "/img/**", "/login/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/errors/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(duplicationLoginCheckFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(ajaxRequestLoginFilter(), ExceptionTranslationFilter.class)
                .formLogin().loginPage("/index")
                .loginProcessingUrl("/login/prcess").permitAll()
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .usernameParameter("Id")
                .passwordParameter("pass")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("SESSION")
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .httpBasic()
                .and()
        //.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//        	.csrf().csrfTokenRepository(csrfTokenRepository())
        ;
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/errors/expired")
                .and()
                .invalidSessionUrl("/errors/invaild/session")
                .sessionAuthenticationErrorUrl("/errors/404")
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http
                .headers()
                .frameOptions().disable()
                .httpStrictTransportSecurity().disable();
//        http
//        		.addFilterAfter(ajaxRequestLoginFilter(), ExceptionTranslationFilter.class);

    }

    @Bean
    public ExtraParamSource extraParamSource(){
        return new ExtraParamSource();
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * static resource setting
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity http) throws Exception {
        http.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setCreateTableOnStartup(false);
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return new UnAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler();
    }

    @Bean
    public SessionRegistryImpl sessionRegistry(){
        SessionRegistryImpl sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public DuplicationLoginCheckFilter duplicationLoginCheckFilter(){
        DuplicationLoginCheckFilter duplicationLoginCheckFilter = new DuplicationLoginCheckFilter(sessionRegistry, secureUserDetailsService, "/login/auth");
        return duplicationLoginCheckFilter;
    }

    @Bean
    public AjaxRequestLoginFilter ajaxRequestLoginFilter(){
        return new AjaxRequestLoginFilter();
    }
}
