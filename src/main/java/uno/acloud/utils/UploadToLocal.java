package uno.acloud.service;   // 你的包名

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.pojo.Result;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Component
public class UploadToLocal {
    public Result upload(MultipartFile file) {
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
            return Result.success(accessUrl);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}