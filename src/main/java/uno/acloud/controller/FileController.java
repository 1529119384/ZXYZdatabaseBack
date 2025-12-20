package uno.acloud.controller;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.anno.Log;
import uno.acloud.pojo.FileInfo;
import uno.acloud.pojo.Result;
import uno.acloud.service.FileService;
import uno.acloud.utils.JwtUtils;
import uno.acloud.utils.ServletUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FileController {

    @Autowired
    private  FileService fileService;

    @Log
    @PostMapping("/uploadFile")
    public Result upload(MultipartFile file, Long parentId, HttpServletRequest request) throws UnsupportedEncodingException {
        String jwt = ServletUtils.getToken(request);
        int userId = (Integer) StpUtil.getExtra(jwt,"userId");

        String url = fileService.upload(file, parentId, userId);
        if (url == null) {
            log.info("上传文件失败");
            return Result.error("上传文件失败");
        }
        return Result.success(url);
    }

    @Log
    @PostMapping("/uploadFolder")
    public Result uploadFolder(String folderName, Long parentId, HttpServletRequest request) {
        String jwt = ServletUtils.getToken(request);
        int userId = (Integer) StpUtil.getExtra(jwt,"userId");

        Long folderId = fileService.uploadFolder(folderName, parentId, userId);
        if (folderId != null) {
            log.info("上传文件夹{}成功", folderName);
            return Result.success(folderId);
        } else {
            log.info("上传文件夹{}失败", folderName);
            return Result.error("上传文件夹失败");
        }
    }

    @Log
    @GetMapping("/getFileList")
    public Result getFileList(Long parentId) {
        try {
            List<FileInfo> fileList = fileService.getFileListByParentId(parentId);
            log.info("获取文件夹{}下的文件列表成功", parentId);
            return Result.success(fileList);
        } catch (Exception e) {
            log.error("获取文件夹{}下的文件列表失败", parentId, e);
            return Result.error("获取文件列表失败");
        }
    }
}