package uno.acloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    /** 文件ID */
    private Long id;

    /** 文件类型：0-文件夹，1-文件 */
    private Integer fileType;

    /** 文件名称 */
    private String fileName;

    /** 文件分类（后缀枚举：.txt …） */
    private Integer category;

    /** 文件大小，单位 KB */
    private Long fileSize;

    /** 文件访问URL */
    private String fileUrl;

    /** 服务器存放路径 */
    private String storePath;

    /** 上传用户ID */
    private Integer userId;

    /** 父级ID，顶层为 -1 */
    private Long parentId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime modifyTime;

    /** 逻辑删除：0-正常，1-已删除 */
    private Integer deleted;
}