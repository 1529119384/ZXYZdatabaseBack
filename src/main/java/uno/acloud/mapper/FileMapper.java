package uno.acloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import uno.acloud.pojo.FileInfo;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO file_info (file_type, uuid_name, original_name, category, file_size, file_url, store_path, user_id, parent_id, create_time, modify_time, deleted) " +
            "VALUES (#{fileType}, #{uuidName}, #{originalName}, #{category}, #{fileSize}, #{fileUrl}, #{storePath}, #{userId}, #{parentId}, #{createTime}, #{modifyTime}, #{deleted})")
    void addFileInfo(FileInfo fileInfo);

    @Insert("INSERT INTO file_info(file_type,original_name,store_path," +
            "user_id,parent_id,create_time,modify_time,deleted) " +
            "VALUES(#{fileType},#{originalName},#{storePath},#{userId}," +
            "#{parentId},#{createTime},#{modifyTime},#{deleted})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer addFolderInfo(FileInfo folderInfo);

    @Select("SELECT * FROM file_info WHERE parent_id = #{parentId} ORDER BY file_type , modify_time DESC")
    List<FileInfo> getFileListByParentId(Long parentId);
}