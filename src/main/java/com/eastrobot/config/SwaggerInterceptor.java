/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/3/12 10:25
 */
@Component
public class SwaggerInterceptor implements HandlerInterceptor {

    @Value("${api.token}")
    private String token;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        if (!StringUtils.isEmpty(requestURI)) {
            String apiToken = request.getHeader(SystemConstants.API_TOKEN);
            if (StringUtils.isEmpty(apiToken) || !Objects.equals(token, apiToken)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
