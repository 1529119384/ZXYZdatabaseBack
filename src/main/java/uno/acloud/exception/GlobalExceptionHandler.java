package uno.acloud.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uno.acloud.pojo.Result;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (Exception.class)
    public Result ex(Exception e) {
        log.error("全局异常抛出");
        e.printStackTrace();
        return Result.error("操作失败，请联系acloud");
    }
}
