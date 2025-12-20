package uno.acloud.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import uno.acloud.pojo.User;

public interface UserService {

  SaTokenInfo login(User user);

  int register(User user);
}
