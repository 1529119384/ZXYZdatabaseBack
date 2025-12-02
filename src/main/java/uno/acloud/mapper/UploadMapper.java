package uno.acloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import uno.acloud.pojo.FileInfo;

@Mapper
public interface UploadMapper {
    @Insert("INSERT INTO file_info (file_type, uuid_name, original_name, category, file_size, file_url, store_path, user_id, parent_id, create_time, modify_time, deleted) " +
            "VALUES (#{fileType}, #{uuidName}, #{originalName}, #{category}, #{fileSize}, #{fileUrl}, #{storePath}, #{userId}, #{parentId}, #{createTime}, #{modifyTime}, #{deleted})")
    void addFileInfo(FileInfo fileInfo);

    @Insert("INSERT INTO file_info(file_type,original_name,store_path," +
            "user_id,parent_id,create_time,modify_time,deleted) " +
            "VALUES(#{fileType},#{originalName},#{storePath},#{userId}," +
            "#{parentId},#{createTime},#{modifyTime},#{deleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addFolderInfo(FileInfo folderInfo);
}