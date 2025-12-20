package uno.acloud.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uno.acloud.mapper.UserMapper;
import uno.acloud.pojo.User;
import uno.acloud.service.UserService;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public SaTokenInfo login(User user) {
        User u = userMapper.getByUsernameAndPassword(user);
        if (u != null) {
            log.info("用户{}登录成功", u.getUsername());

            StpUtil.login(u.getId(), new SaLoginParameter()
                    .setExtra("userId", u.getId())
                    .setExtra("username", u.getUsername())
                    .setExtra("role", u.getRole())
                    .setExtra("permission", u.getPermission()));
            return  StpUtil.getTokenInfo();
        } else {
            return null;
        }
    }

    @Override
    public int register(User user) {
        user.setCreateTime(LocalDateTime.now());
        log.info("用户{}注册时间:{}", user.getUsername(), user.getCreateTime());
        return userMapper.addByUsernameAndPassword(user);
    }
}
