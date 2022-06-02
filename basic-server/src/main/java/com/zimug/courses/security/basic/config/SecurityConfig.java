package com.zimug.courses.security.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private  MyAccessDeniedHandler accessDeniedHandler;

    @Resource
    private MyAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

        http.csrf().disable()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                //.defaultSuccessUrl("/")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)

                .and()

                .authorizeRequests()
                .antMatchers("/login.html","/login").permitAll()
                .antMatchers("/","/biz1","/biz2").hasAnyAuthority("ROLE_user","ROLE_admin")
                //.antMatchers("/syslog","/sysuser").hasAnyRole("admin")
                .antMatchers("/syslog").hasAnyAuthority("sys:log")
                .antMatchers("/sysuser").hasAnyAuthority("sys:user")
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("user")

                .and()

                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                //.roles("admin")
                .authorities("sys:log","sys:user")

                .and()
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/css/**","/fonts/**","/img/**","/js/**");
    }

}
