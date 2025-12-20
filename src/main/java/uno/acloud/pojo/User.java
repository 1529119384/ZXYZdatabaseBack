package uno.acloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String avatar;
    private List<String> role;
    private List<String> permission;
    private LocalDateTime createTime;
}
