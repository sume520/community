package com.sun.community.config;

import com.sun.community.entity.User;
import com.sun.community.service.UserService;
import com.sun.community.util.CommunityConstant;
import com.sun.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略静态资源的访问
        web.ignoring().antMatchers("/resource/**");
    }

    //AuthenticationManager: 认证的核心接口
    //AuthenticationManagerBuilder: 用于构建AuthenticationManager对象的工具
    //ProviderManager: AuthenticationManager接口的默认实现类
   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内置的认证规则
        //auth.userDetailsService(userService).passwordEncoder(new Pbkdf2PasswordEncoder("12345"));

        //自定义认证规则
        //AuthenticationProvider: ProviderManager持有一组AuthenticationProvider，每个AuthenticationProvider负责一种认证
        //委托模式：ProviderManager将认证委托给AuthenticationProvider
        auth.authenticationProvider(new AuthenticationProvider() {//添加一个认证方法
            //Authentication: 用于封装认证信息的接口，不同的实现类代表不同类型的认证信息
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();

                User user = userService.findUserByName(username);
                if (user == null) {
                    throw new UsernameNotFoundException("账号不存在");
                }

                password = CommunityUtil.md5(password + user.getSalt());
                if (!user.getPassword().equals(password)) {
                    throw new BadCredentialsException("密码不正确");
                }

                //principal: 主要信息；credentials：证书；authorities：权限
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            }

            //当前的AuthenticationProvider支持哪种类型的认证
            @Override
            public boolean supports(Class<?> authentication) {
                //UsernamePasswordAuthenticationToken: Authentication接口从常用实现类
                return UsernamePasswordAuthenticationToken.class.equals(authentication);
            }
        });
    }*/

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /*//登录相关配置
        httpSecurity.formLogin()
                .loginPage("/loginpage")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> response.sendRedirect(request.getContextPath() + "/index"))
                .failureHandler((request, response, exception) -> {
                    request.setAttribute("error", exception.getMessage());
                    request.getRequestDispatcher("/loginpage").forward(request, response);
                }); */

        //授权配置
        httpSecurity.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR)
                .antMatchers("/discuss/top",
                        "/discuss/wonderful")
                .hasAnyAuthority(AUTHORITY_MODERATOR)
                .antMatchers("/discuss/delete",
                        "/data/**")
                .hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll();

        //权限异常处理
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    //没有登录
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJSONString(403, "你还没有登录哦!"));
                    } else {
                        response.sendRedirect(request.getContextPath() + "/login");
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    //权限不足
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限!"));
                    } else {
                        response.sendRedirect(request.getContextPath() + "/denied");
                    }
                });

        //Security底层默认会拦截/logout请求，进行退出处理
        //覆盖它的默认逻辑，才能执行自己的退出代码
        httpSecurity.logout()
                .logoutUrl("/securitylogout");
//                .logoutSuccessHandler((request, response, authentication) ->
//                        response.sendRedirect(request.getContextPath() + "/index"));

        //增加Filter, 处理验证码
      /*  httpSecurity.addFilterBefore((servletRequest, servletResponse, filterChain) -> {
            HttpServletRequest request= (HttpServletRequest) servletRequest;
            HttpServletResponse response= (HttpServletResponse) servletResponse;
            if(request.getServletPath().equals("login")){
                String veriryCode=request.getParameter("code");
                if(veriryCode==null||!veriryCode.equalsIgnoreCase("1234")){
                    request.setAttribute("error","验证码错误");
                    request.getRequestDispatcher("/loginpage").forward(request,response);
                    return;
                }
            }
            //将请求传给下一个filter
            filterChain.doFilter(request,response);
        }, UsernamePasswordAuthenticationFilter.class);

        //记住我
        httpSecurity.rememberMe()
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .tokenValiditySeconds(3600*24)
                .userDetailsService(userService);*/
    }
}
