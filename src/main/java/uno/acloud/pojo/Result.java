package uno.acloud.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {
    private Integer code;   // 状态码1-成功 0-失败
    private String message; // 返回信息
    private Object data;    // 返回数据
    
    // 手动添加三参数构造器
    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    public static Result success() {
        return new Result(1, "success", null);
    }

    public static Result error(String message) {
        return new Result(0, message, null);
    }
}