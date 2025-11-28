package uno.acloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.pojo.Result;
import uno.acloud.service.UploadService;
import uno.acloud.utils.OSSUploader;
import uno.acloud.utils.UploadToLocal;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private OSSUploader ossUploader;
    @Autowired
    private UploadToLocal uploadToLocal;


    @Log
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        log.info("开始上传文件，文件名：{}，大小：{}，类型：{}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());
        String url = uploadToLocal.upload(file);


        return url;

        // OSSUploader 成功返回 URL，失败抛 FileUploadException，由 GlobalExceptionHandler 转 Result
//        String url = ossUploader.upload(file);
//        log.info("文件上传成功，访问地址：{}", url);
//        return Result.success(url);
    }
}
