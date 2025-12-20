package uno.acloud.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uno.acloud.mapper.OperateLogMapper;
import uno.acloud.pojo.OperateLog;
import uno.acloud.utils.JwtUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {
    @Autowired
    private OperateLogMapper operateLogMapper;
    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(uno.acloud.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {


        // 获取用户ID
        String jwt = request.getHeader("loginUser");

        Integer operateUserId = Integer.valueOf(StpUtil.getExtra(jwt,"userId").toString());
        // 获取操作时间
        LocalDateTime operateTime = LocalDateTime.now();
        // 获取类名和方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 获取参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        // 获取返回值
        String returnVa = JSONObject.toJSONString(result);
        // 获取耗时
        long costTime = end - start;
        // 封装日志
//        log.info("用户{}在{}操作类{}方法{}参数{}返回{}耗时{}", operateUserId, operateTime, className, methodName, methodParams, returnVa, costTime);
        OperateLog operateLog = new OperateLog(null, operateUserId, operateTime, className, methodName, methodParams, returnVa, costTime);

        operateLogMapper.insert(operateLog);
        return result;
    }
}
