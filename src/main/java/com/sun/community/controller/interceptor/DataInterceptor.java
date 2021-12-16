package com.sun.community.controller.interceptor;

import com.sun.community.entity.User;
import com.sun.community.service.DataService;
import com.sun.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DataInterceptor implements HandlerInterceptor {

    @Autowired
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //统计UV
        String ip=request.getRemoteHost();
        System.out.println("remote host: "+request.getRemoteHost());
        dataService.recordUV(ip);

        //统计DAU
        User user=hostHolder.getUser();
        if(user!=null){
            dataService.recordDAU(user.getId());
        }

        return true;
    }
}
