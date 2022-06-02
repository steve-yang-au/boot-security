package com.zimug.courses.security.basic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.exception.AjaxResponse;
import com.zimug.commons.exception.CustomException;
import com.zimug.commons.exception.CustomExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//用户登录后访问没有权限访问的资源，可以实现AccessDeniedHandler 接口进行相应处理，提示用户没有访问权限
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Value("${spring.security.logintype}")
    private String loginType;

    private  static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        if (loginType.equalsIgnoreCase("JSON")) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(
                    objectMapper.writeValueAsString(
                            AjaxResponse.error(
                                    new CustomException(
                                            CustomExceptionType.USER_INPUT_ERROR,
                                            "you have no right to access the resources"))));
        } else {
        }
    }
}

