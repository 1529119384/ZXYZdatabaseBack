package uno.acloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.mapper.UploadMapper;
import uno.acloud.pojo.FileInfo;
import uno.acloud.service.UploadService;
import uno.acloud.utils.FileNameUtil;
import uno.acloud.utils.FileTypeUtil;
import uno.acloud.utils.OSSUploader;
import uno.acloud.utils.UploadToLocal;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private OSSUploader ossUploader;
    @Autowired
    private UploadToLocal uploadToLocal;
    @Autowired
    private UploadMapper uploadMapper;

    @Log
    @PostMapping("/upload")
    public String upload(MultipartFile file, Long parentId, Integer userId) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        log.info("开始上传文件，文件名：{}，大小：{}，类型：{}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());
        String url = uploadToLocal.upload(file);

        if (url == null) {
            log.info("上传文件失败");
            return null;
        }

        FileInfo fileInfo = new FileInfo();

        fileInfo.setFileType(1);

        fileInfo.setUuidName(FileNameUtil.uuidName(file.getOriginalFilename()));
        fileInfo.setOriginalName(file.getOriginalFilename());


        int typeCode;
        try {
            typeCode = FileTypeUtil.classify(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            return null;
        }
        fileInfo.setCategory(typeCode);

        fileInfo.setFileSize(file.getSize());
        /*
         * 文件路径没更新，前端还没适配
         * */
        /*
         * 上传文件没给元数据
         * */
        fileInfo.setStorePath("555");
        fileInfo.setFileUrl(url);
        fileInfo.setUserId(userId);
        fileInfo.setParentId(parentId);
        fileInfo.setCreateTime(LocalDateTime.now());
        fileInfo.setModifyTime(LocalDateTime.now());
        fileInfo.setDeleted(0);


        uploadMapper.add(fileInfo);


        return url;

//        OSSUploader 成功返回 URL，失败抛 FileUploadException，由 GlobalExceptionHandler 转 Result
//        String url = ossUploader.upload(file);
//        log.info("文件上传成功，访问地址：{}", url);
//        return Result.success(url);
    }
}
