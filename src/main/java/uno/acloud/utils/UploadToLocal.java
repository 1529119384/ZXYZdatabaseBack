package uno.acloud.utils;   // 你的包名

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.exception.FileUploadException;
import uno.acloud.pojo.Result;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Component
public class UploadToLocal {
    public String upload(MultipartFile file) {
        try {
            String baseDir = "D:/code/databaseZXYZ/files";
            Path dirPath = Paths.get(baseDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String newFilename = System.currentTimeMillis() + ext;

            Path targetPath = dirPath.resolve(newFilename);
            Files.write(targetPath, file.getBytes());

            String accessUrl = "http://localhost:8080/files/" + newFilename;
            return accessUrl;
        } catch (FileUploadException e) {
            // 业务异常直接抛
            throw e;
        } catch (Exception e) {
            // 其他异常包装成业务异常
            log.error("上传文件系统异常", e);
            throw new FileUploadException("文件上传失败：" + e.getMessage());
        }
    }
}