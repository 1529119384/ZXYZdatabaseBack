package uno.acloud.exception;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.util.SaResult;
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

    @ExceptionHandler(NotLoginException.class)
    public Result handlerException(NotLoginException e) {
        // 打印堆栈，以供调试
        log.warn("用户未登录异常：{}", e.getMessage());
        // 返回给前端
        return Result.error("NO_LOGIN");
    }

    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public Result handlerException(NotPermissionException e) {
        log.warn("缺少权限异常：{}", e.getMessage());
        return Result.error("缺少权限：" + e.getPermission());
    }

    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public Result handlerException(NotRoleException e) {
        log.warn("缺少角色异常：{}", e.getMessage());
        return Result.error("缺少角色：" + e.getRole());
    }

    /* 2. 兜底：其他所有异常 */
    @ExceptionHandler(Exception.class)
    public Result handleOther(Exception e) {
        log.error("系统异常", e);
        return Result.error("操作失败，请联系acloud,异常为"+e.getMessage());
    }
}