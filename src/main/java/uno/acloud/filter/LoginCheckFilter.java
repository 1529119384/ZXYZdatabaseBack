package uno.acloud.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
/*
*
* 弃用
*
* */
//@WebFilter("/*")
@Slf4j
public class LoginCheckFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String requestURI = request.getRequestURI();// /login
        //不校验登录请求和注册请求
        if (requestURI.contains("login") || requestURI.contains("register")) {
            log.info("登录请求放行");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

// 只放掉 OPTIONS、login、register，其余交给 Sa-Token
        if ("OPTIONS".equals(request.getMethod()) ||
                requestURI.contains("login") ||
                requestURI.contains("register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
// 其他请求让 Sa-Token 自己拦
        filterChain.doFilter(servletRequest, servletResponse);

//        log.info("开始校验是否登录");
//        String jwt = request.getHeader("token");
//
//        if (!StringUtils.hasLength(jwt)) {
//            log.info("用户没有token，返回未登录页面");
//            Result error = Result.error("NO_LOGIN");
//            String noLogin = JSONObject.toJSONString(error);
//            servletResponse.getWriter().write(noLogin);
//            return;
//        }
//        log.info("用户有令牌,开始校验令牌");
//        try {
////            JwtUtils.parseJWT(jwt);
//            log.info(jwt);
//            Integer operateUserId = Integer.valueOf(StpUtil.getExtra(jwt,"userId").toString());
//            log.info("令牌校验成功,用户ID:{}",operateUserId);
//        } catch (Exception e) {
//            log.info("令牌校验失败,返回未登录页面,异常信息:{}",e.getMessage());
//            Result error = Result.error("NO_LOGIN");
//            String noLogin = JSONObject.toJSONString(error);
//            servletResponse.getWriter().write(noLogin);
//            return;
//        }
//        log.info("令牌校验成功,放行");
//        filterChain.doFilter(servletRequest, servletResponse);
    }


}
