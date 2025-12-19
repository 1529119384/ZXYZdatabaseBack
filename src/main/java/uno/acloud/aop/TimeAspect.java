package uno.acloud.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect//aop 切面
@Component
public class TimeAspect {
    @Around("execution(* uno.acloud.service.*.*(..))")//切入点表达式
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("方法{}耗时:{}ms", joinPoint.getSignature(), end - start);
        return result;
    }
}
