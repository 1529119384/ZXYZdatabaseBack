package uno.acloud.mapper;

import org.apache.ibatis.annotations.Insert;
import uno.acloud.pojo.FileInfo;

public class UploadMapper {
    @Insert("INSERT INTO file_info (file_type, file_name, category, file_size, file_url, store_path, user_id, parent_id, create_time, modify_time, deleted) " +
            "VALUES (#{fileType}, #{fileName}, #{category}, #{fileSize}, #{fileUrl}, #{storePath}, #{userId}, #{parentId}, #{createTime}, #{modifyTime}, #{deleted})")
    public static void add(FileInfo fileInfo) {
    }
}
