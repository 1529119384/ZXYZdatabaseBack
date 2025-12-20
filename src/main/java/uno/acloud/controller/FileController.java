package uno.acloud.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
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
import uno.acloud.utils.ServletUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Log
    @PostMapping("/uploadFile")
    @SaCheckRole("admin")
    public Result upload(MultipartFile file, Long parentId) throws UnsupportedEncodingException {
//        String jwt = ServletUtils.getToken(request);
//        int userId = Integer.parseInt(StpUtil.getExtra(jwt, "userId").toString());
        int userId = StpUtil.getLoginIdAsInt();
        String url = fileService.upload(file, parentId, userId);
        if (url == null) {
            log.info("上传文件失败");
            return Result.error("上传文件失败");
        }
        return Result.success(url);
    }

    @Log
    @PostMapping("/uploadFolder")
    @SaCheckRole("admin")
    public Result uploadFolder(String folderName, Long parentId) {
        int userId = StpUtil.getLoginIdAsInt();

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
    @SaCheckRole(value = {"admin", "user"}, mode = SaMode.OR)
    public Result getFileList(Long parentId) {
        try {
            List<FileInfo> fileList = fileService.getFileListByParentId(parentId);
            log.info("获取文件夹{}下的文件列表成功", parentId);
            log.info("文件列表:{}", fileList);
            return Result.success(fileList);
        } catch (Exception e) {
            log.error("获取文件夹{}下的文件列表失败", parentId, e);
            return Result.error("获取文件列表失败");
        }
    }


}