package uno.acloud.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.pojo.Result;
import uno.acloud.service.UploadService;
import uno.acloud.utils.JwtUtils;
import uno.acloud.utils.ServletUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Log
    @PostMapping("/uploadFile")
    public Result upload(MultipartFile file, Long parentId, HttpServletRequest request) throws UnsupportedEncodingException {
        String jwt = ServletUtils.getToken(request);
        Map<String, Object> claims = JwtUtils.parseJWT(jwt);
        int userId = (Integer) claims.get("userId");

        String url = uploadService.upload(file, parentId, userId);
        if (url == null) {
            log.info("上传文件失败");
            return Result.error("上传文件失败");
        }
        return Result.success(url);
    }
    @Log
    @PostMapping("/uploadFolder")
    public Result uploadFolder(String folderName, Long parentId, HttpServletRequest request) throws UnsupportedEncodingException {
        String jwt = ServletUtils.getToken(request);
        Map<String, Object> claims = JwtUtils.parseJWT(jwt);
        int userId = (Integer) claims.get("userId");

        Integer folderId = uploadService.uploadFolder(folderName, parentId, userId);
        if (folderId != null) {
            log.info("上传文件夹成功");
            return Result.success(folderId);
        } else {
            log.info("上传文件夹失败");
            return Result.error("上传文件夹失败");
        }
    }
}