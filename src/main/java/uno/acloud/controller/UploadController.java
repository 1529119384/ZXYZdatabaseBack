package uno.acloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.pojo.Result;
import uno.acloud.utils.OSSUploader;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private OSSUploader ossUploader;
    @Log
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        //输出日志
        log.info("开始,文件上传操作,文件名:{}", file.getOriginalFilename());
        log.info("文件上传操作,文件大小:{}", file.getSize());

        // 调用阿里云OSS上传文件
        Result result = ossUploader.upload(file);
        if (result.getCode() == 1) {
            log.info("结束,文件上传成功,访问地址:{}", result.getData());
            return Result.success(result.getData());
        } else {
            return Result.error(result.getMessage());
        }
    }
}
