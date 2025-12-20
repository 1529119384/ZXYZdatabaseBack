package uno.acloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface PermissionRoleMapper {

    @Select("""
            SELECT r.role_name
            FROM user_role AS ur
            JOIN role AS r ON r.id = ur.role_id
            WHERE ur.user_id = #{userId};""")
    List<String> getRoleByUserID(int userId);

    @Select("""
            SELECT p.permission_name
            FROM permission AS p
            JOIN role_permission AS rp ON rp.permission_id = p.id
            JOIN user_role AS ur ON ur.role_id = rp.role_id
            WHERE ur.user_id = #{userId};""")
    List<String> getPermissionByUserID(int userId);
}
