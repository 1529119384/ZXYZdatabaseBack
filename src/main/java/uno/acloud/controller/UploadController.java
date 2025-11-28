package uno.acloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.pojo.Result;
import uno.acloud.service.UploadService;
import uno.acloud.utils.OSSUploader;
import uno.acloud.utils.UploadToLocal;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Log
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String url = uploadService.upload(file);
        if (url == null) {
            log.info("上传文件失败");
            return Result.error("上传文件失败");
        }
        return Result.success(url);
    }
}