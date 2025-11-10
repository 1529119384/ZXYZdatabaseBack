package uno.acloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uno.acloud.pojo.User;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username=#{username} AND password=#{password}")
    User getByUsernameAndPassword(User user);

    @Insert("INSERT INTO users(username,password,create_time) VALUES(#{username},#{password},#{createTime})")
    int addByUsernameAndPassword(User user);
}
