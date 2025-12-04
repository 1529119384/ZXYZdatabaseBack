package uno.acloud.service;

import org.springframework.web.multipart.MultipartFile;
import uno.acloud.pojo.FileInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FileService {
    String upload(MultipartFile file, Long parentId,Integer userId) throws UnsupportedEncodingException;

    Long uploadFolder(String folderName, Long parentId, Integer userId);

     List<FileInfo> getFileListByParentId(Long parentId);
}
