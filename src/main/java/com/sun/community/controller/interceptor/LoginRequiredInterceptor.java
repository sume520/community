package com.sun.community.controller.interceptor;

import com.sun.community.annotation.LoginRequired;
import com.sun.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    final Logger logger= LoggerFactory.getLogger(LoginTicketInterceptor.class);

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            Method method=handlerMethod.getMethod();
            LoginRequired loginRequired=method.getAnnotation(LoginRequired.class);
            if(loginRequired!=null&&hostHolder.getUser()==null){
                response.sendRedirect(request.getContextPath()+"/login");
                logger.debug("没有访问该页面的权限！");
                return false;
            }
        }
        return true;
    }
}
