package uno.acloud.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String upload(MultipartFile file, Long parentId,Integer userId);

}
