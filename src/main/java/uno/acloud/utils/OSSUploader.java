package uno.acloud.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uno.acloud.pojo.OSSProperties;
import uno.acloud.pojo.Result;

import java.io.InputStream;

@Slf4j
@Component
public class OSSUploader {
    // 定义小文件阈值，20MB
    private static final long SMALL_FILE_THRESHOLD = 20 * 1024 * 1024;
    @Autowired
    private OSSProperties ossProperties;

    public Result upload(MultipartFile file) {

        // 从OSSProperties中获取配置
        String region = ossProperties.getRegion();
        String bucket = ossProperties.getBucket();
        // String endpoint = ossProperties.getEndpoint();

        String filename = file.getOriginalFilename();
        CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region);

        try (OSSClient client = clientBuilder.build()) {
            log.info("开始上传文件:{}", file.getOriginalFilename());
            PutObjectResult result;
            // 根据文件大小选择不同的上传方式
            if (file.getSize() < SMALL_FILE_THRESHOLD) {
                // 小文件：一次性读入内存
                log.info("使用小文件上传方式(一次性读入内存)");
                byte[] fileData = new byte[(int) file.getSize()];
                result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(bucket)
                        .key(file.getOriginalFilename())
                        .body(BinaryData.fromBytes(fileData))
                        .build());
            } else {
                // 大文件：流式上传
                log.info("使用大文件上传方式(流式上传)");
                try (InputStream inputStream = file.getInputStream()) {
                    result = client.putObject(PutObjectRequest.newBuilder()
                            .bucket(bucket)
                            .key(file.getOriginalFilename())
                            .body(BinaryData.fromStream(inputStream))
                            .build());
                }
            }
            if (result.statusCode() != 200) {
                log.error("上传文件失败,状态码: {},request id: {},eTag: {}",
                        result.statusCode(), result.requestId(), result.eTag());
                return Result.error("上传文件失败,状态码: " + result.statusCode());
            }
            log.info("状态码:{}, request id:{}, eTag:{}", result.statusCode(), result.requestId(), result.eTag());

            // 根据文件大小选择合适的单位
            long fileSize = file.getSize();
            if (fileSize < 1024 * 1024) {
                log.info("上传成功 '{}' ,大小: {} KB", filename, fileSize / 1024.0);
            } else {
                log.info("上传成功 '{}' ,大小: {} MB", filename, fileSize / (1024.0 * 1024.0));
            }

            // 生成并显示文件访问URL
            /* https://<bucket-name>.<endpoint-domain>/<object-key> */
            // 记得修改bucket和region
            String Url = "https://" + bucket + ".oss-" + region + ".aliyuncs.com/" + filename;
            log.info("文件访问地址:{}", Url);
            return Result.success(Url);

        } catch (Exception e) {
            log.error("上传文件失败,异常信息: {}", e.getMessage(), e);
            return Result.error("上传文件失败,异常信息: " + e.getMessage());
        }
    }
}