package uno.acloud.service;

import uno.acloud.pojo.User;

public interface UserService {

  String login(User user);

  int register(User user);
}
