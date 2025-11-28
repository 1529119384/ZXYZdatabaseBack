package uno.acloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.pojo.Result;
import uno.acloud.utils.OSSUploader;
import uno.acloud.utils.UploadToLocal;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private OSSUploader ossUploader;
    @Autowired
    private UploadToLocal uploadToLocal;

    @Log
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        log.info("开始上传文件，文件名：{}，大小：{}，类型：{}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());
        String url = uploadToLocal.upload(file);





        return Result.success(url);

        // OSSUploader 成功返回 URL，失败抛 FileUploadException，由 GlobalExceptionHandler 转 Result
//        String url = ossUploader.upload(file);
//        log.info("文件上传成功，访问地址：{}", url);
//        return Result.success(url);
    }
}