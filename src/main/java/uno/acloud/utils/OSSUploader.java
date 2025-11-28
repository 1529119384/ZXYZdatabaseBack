package uno.acloud.utils;

import com.aliyun.sdk.service.oss2.*;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.exception.FileUploadException;
import uno.acloud.pojo.OSSProperties;

import java.io.InputStream;

@Slf4j
@Component
public class OSSUploader {

    private static final long SMALL_FILE_THRESHOLD = 20 * 1024 * 1024; // 20 MB

    @Autowired
    private OSSProperties ossProperties;

    /**
     * 上传成功后直接返回文件访问 URL
     * 失败一律抛 FileUploadException，由 GlobalExceptionHandler 统一转 Result
     */
    public String upload(MultipartFile file) {
        String region = ossProperties.getRegion();
        String bucket = ossProperties.getBucket();
        String filename = file.getOriginalFilename();

        CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
        OSSClientBuilder builder = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region);

        try (OSSClient client = builder.build()) {
            log.info("开始上传文件: {}", filename);

            PutObjectResult result;
            if (file.getSize() < SMALL_FILE_THRESHOLD) {
                log.info("小文件内存上传");
                byte[] data = file.getBytes();
                result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(bucket)
                        .key(filename)
                        .body(BinaryData.fromBytes(data))
                        .build());
            } else {
                log.info("大文件流式上传");
                try (InputStream in = file.getInputStream()) {
                    result = client.putObject(PutObjectRequest.newBuilder()
                            .bucket(bucket)
                            .key(filename)
                            .body(BinaryData.fromStream(in))
                            .build());
                }
            }

            if (result.statusCode() != 200) {
                throw new FileUploadException("上传失败，状态码：" + result.statusCode());
            }

            String url = "https://" + bucket + ".oss-" + region + ".aliyuncs.com/" + filename;
            log.info("上传成功，大小 {} MB，访问地址：{}", file.getSize() / (1024.0 * 1024), url);
            return url;

        } catch (FileUploadException e) {
            // 业务异常直接抛
            throw e;
        } catch (Exception e) {
            // 其他异常包装成业务异常
            log.error("上传文件系统异常", e);
            throw new FileUploadException("文件上传失败：" + e.getMessage());
        }
    }
}