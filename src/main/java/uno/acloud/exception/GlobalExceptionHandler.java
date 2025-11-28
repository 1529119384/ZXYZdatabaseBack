package uno.acloud.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uno.acloud.pojo.Result;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 1. 业务异常：给用户看具体原因 */
    @ExceptionHandler(FileUploadException.class)
    public Result handleFileUpload(FileUploadException e) {
        log.warn("文件上传业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /* 2. 兜底：其他所有异常 */
    @ExceptionHandler(Exception.class)
    public Result handleOther(Exception e) {
        log.error("系统异常", e);
        return Result.error("操作失败，请联系acloud");
    }
}