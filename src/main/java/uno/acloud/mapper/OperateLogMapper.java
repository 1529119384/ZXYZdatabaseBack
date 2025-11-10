package uno.acloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import uno.acloud.pojo.OperateLog;

@Mapper
public interface OperateLogMapper {
    @Insert("INSERT INTO operate_log(operate_user,operate_time,class_name,method_name,method_params,return_value,cost_time) " +
            "VALUES(#{operateUser},#{operateTime},#{className},#{methodName},#{methodParams},#{returnValue},#{costTime})")
    public void insert(OperateLog operateLog);
}
