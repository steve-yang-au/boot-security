package com.zimug.courses.security.basic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//用户未登录就访问系统资源，可以实现AuthenticationEntryPoint 接口进行响应处理，提示用户应该先去登录
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${spring.security.logintype}")
    private String loginType;

    private  static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        if (loginType.equalsIgnoreCase("JSON")) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(
                    objectMapper.writeValueAsString(
                            AjaxResponse.error(
                                    new CustomException(
                                            CustomExceptionType.USER_INPUT_ERROR,
                                            "login first to access the resources"))));
        } else {
        }
    }
}
