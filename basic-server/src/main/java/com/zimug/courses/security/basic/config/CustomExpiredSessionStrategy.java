package com.zimug.courses.security.basic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        //redirectStrategy.sendRedirect(event.getRequest(),event.getResponse(),"sessionTimeout.html");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 403);
        map.put("msg","your session timeout or login on another device " + event.getSessionInformation().getLastRequest());
        String json = objectMapper.writeValueAsString(map);
        event.getResponse().setContentType("application/json;charset=utf-8");
        event.getResponse().getWriter().write(json);
    }
}
