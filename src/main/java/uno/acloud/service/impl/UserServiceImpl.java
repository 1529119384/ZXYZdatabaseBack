package uno.acloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uno.acloud.mapper.UserMapper;
import uno.acloud.pojo.User;
import uno.acloud.service.UserService;
import uno.acloud.utils.JwtUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public String login(User user) {
        User u = userMapper.getByUsernameAndPassword(user);
        if (u != null) {
            System.out.println(u);
            return JwtUtils.generateJwt(u);
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
