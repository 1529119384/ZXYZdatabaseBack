package uno.acloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.mapper.UploadMapper;
import uno.acloud.pojo.FileInfo;
import uno.acloud.pojo.Result;
import uno.acloud.service.UploadService;
import uno.acloud.utils.FileNameUtil;
import uno.acloud.utils.FileTypeUtil;
import uno.acloud.utils.OSSUploader;
import uno.acloud.utils.UploadToLocal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    public String upload(MultipartFile file, Long parentId, Integer userId) throws UnsupportedEncodingException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        log.info("开始上传文件，文件名：{}，大小：{}，类型：{}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());
//        String url = uploadToLocal.upload(file);
        String uuidName = FileNameUtil.uuidName(file.getOriginalFilename());
        String url = ossUploader.upload(file, uuidName);
        log.info("文件上传成功，访问地址：{}", url);
        if (url == null) {
            log.error("OSS上传返回url为null，判定失败");
            return null;
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileType(1);
        fileInfo.setUuidName(uuidName);
        fileInfo.setOriginalName(file.getOriginalFilename());
        int typeCode;
        try {
            typeCode = FileTypeUtil.classify(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("文件类型识别失败", e);
            return null;
        }
        fileInfo.setCategory(typeCode);
        fileInfo.setFileSize(file.getSize());
        /*
         * 文件路径没更新，前端还没适配
         * */
        fileInfo.setStorePath("555");
        fileInfo.setFileUrl(url);
        fileInfo.setUserId(userId);
        fileInfo.setParentId(-1L);
        fileInfo.setCreateTime(LocalDateTime.now());
        fileInfo.setModifyTime(LocalDateTime.now());
        fileInfo.setDeleted(0);
        uploadMapper.addFileInfo(fileInfo);
        return url;
    }

    @Override
    public Integer uploadFolder(String folderName, Long parentId, Integer userId) {
        FileInfo folderInfo = new FileInfo();
        folderInfo.setFileType(0);
        folderInfo.setOriginalName(folderName);
        folderInfo.setCategory(null);
        folderInfo.setFileSize(null);
        /*
         * 文件路径没更新，前端还没适配
         * */
        folderInfo.setStorePath("555");
        folderInfo.setUserId(userId);
        folderInfo.setParentId(parentId);
        folderInfo.setCreateTime(LocalDateTime.now());
        folderInfo.setModifyTime(LocalDateTime.now());
        folderInfo.setDeleted(0);

        return uploadMapper.addFolderInfo(folderInfo);
    }
}
