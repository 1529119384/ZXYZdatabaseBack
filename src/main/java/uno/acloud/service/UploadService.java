package uno.acloud.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

public interface UploadService {
    String upload(MultipartFile file, Long parentId,Integer userId) throws UnsupportedEncodingException;

    Integer uploadFolder(String folderName, Long parentId, Integer userId);
}
