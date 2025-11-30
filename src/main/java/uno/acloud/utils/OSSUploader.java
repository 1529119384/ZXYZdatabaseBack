package uno.acloud.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.exception.FileUploadException;
import uno.acloud.pojo.OSSProperties;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OSSUploader {

    private static final long SMALL_FILE_THRESHOLD = 20 * 1024 * 1024;

    @Autowired
    private OSSProperties ossProperties;

    public String upload(MultipartFile file, String uuidName) throws UnsupportedEncodingException {
        String region  = ossProperties.getRegion();
        String bucket  = ossProperties.getBucket();
        String filename = file.getOriginalFilename();

        String downloadFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replace("+", "%20");
        String contentDisposition = "attachment; filename*=utf-8''" + downloadFileName;

        var provider = new EnvironmentVariableCredentialsProvider();
        var builder  = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region);

        try (OSSClient client = builder.build()) {
            log.info("开始上传文件: {}", filename);

            PutObjectResult result;
            if (file.getSize() < SMALL_FILE_THRESHOLD) {
                result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(bucket)
                        .key(uuidName)
                        .body(BinaryData.fromBytes(file.getBytes()))
                        .contentDisposition(contentDisposition)   // ← 直接写头
                        .build());
            } else {
                try (InputStream in = file.getInputStream()) {
                    result = client.putObject(PutObjectRequest.newBuilder()
                            .bucket(bucket)
                            .key(uuidName)
                            .body(BinaryData.fromStream(in))
                            .contentDisposition(contentDisposition)
                            .build());
                }
            }

            if (result.statusCode() != 200) {
                throw new FileUploadException("上传失败，状态码：" + result.statusCode());
            }

            String url = "https://" + bucket + ".oss-" + region + ".aliyuncs.com/" + uuidName;
            log.info("上传成功，大小 {} MB，访问地址：{}", file.getSize() / (1024.0 * 1024), url);
            return url;

        } catch (FileUploadException e) {
            throw e;
        } catch (Exception e) {
            log.error("上传文件系统异常", e);
            throw new FileUploadException("文件上传失败：" + e.getMessage());
        }
    }
}