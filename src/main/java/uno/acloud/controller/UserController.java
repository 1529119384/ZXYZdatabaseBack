package uno.acloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uno.acloud.pojo.Result;
import uno.acloud.pojo.User;
import uno.acloud.service.UserService;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("用户{}申请登录,请求参数:{}", user.getUsername(), user);
        String jwt = userService.login(user);
        if (jwt != null) {
            log.info("用户{}登录成功,返回登录凭证:{}", user.getUsername(), jwt);
            return Result.success(jwt);
        } else {
            log.error("用户{}登录失败", user.getUsername());
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info("用户{}申请注册,请求参数:{}", user.getUsername(), user);
        try {
            int i = userService.register(user);
            if (i > 0) {
                log.info("用户{}注册成功", user.getUsername());
                return Result.success("注册成功");
            } else {
                log.error("用户{}注册失败", user.getUsername());
                return Result.error("注册失败");
            }
        } catch (org.springframework.dao.DuplicateKeyException e) {
            log.error("用户{}注册失败，用户名已存在", user.getUsername());
            return Result.error("用户名已存在");
        }
    }

}
