package uno.acloud;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.CredentialsProvider;
import com.aliyun.sdk.service.oss2.credentials.EnvironmentVariableCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Example {

    // 定义小文件阈值，这里设置为20MB
    private static final long SMALL_FILE_THRESHOLD = 20 * 1024 * 1024;

    private static void execute(
            String endpoint,
            String region,
            String bucket,
            String key,
            String filePath) {

        CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();
        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(provider)
                .region(region);

        if (endpoint != null) {
            clientBuilder.endpoint(endpoint);
        }

        try (OSSClient client = clientBuilder.build()) {

            File file = new File(filePath);
            PutObjectResult result;

            // 根据文件大小选择不同的上传方式
            if (file.length() < SMALL_FILE_THRESHOLD) {
                // 小文件：一次性读入内存
                System.out.println("使用小文件上传方式（一次性读入内存）");
                byte[] fileData = new byte[(int) file.length()];
                try (FileInputStream fis = new FileInputStream(file)) {
                    int bytesRead = 0;
                    int offset = 0;
                    // 循环读取文件内容，确保完整读取
                    while (offset < fileData.length && (bytesRead = fis.read(fileData, offset, fileData.length - offset)) != -1) {
                        offset += bytesRead;
                    }
                    // 验证是否完整读取文件
                    if (offset < fileData.length) {
                        System.out.println("警告: 文件未完全读取。读取了 " + offset + " 字节，文件总大小为 " + fileData.length + " 字节");
                    }
                }

                result = client.putObject(PutObjectRequest.newBuilder()
                        .bucket(bucket)
                        .key(key)
                        .body(BinaryData.fromBytes(fileData))
                        .build());
            } else {
                // 大文件：流式上传
                System.out.println("使用大文件上传方式（流式上传）");
                try (InputStream inputStream = new FileInputStream(file)) {
                    result = client.putObject(PutObjectRequest.newBuilder()
                            .bucket(bucket)
                            .key(key)
                            .body(BinaryData.fromStream(inputStream))
                            .build());
                }
            }

            System.out.printf("status code:%d, request id:%s, eTag:%s\n",
                    result.statusCode(), result.requestId(), result.eTag());

            // 根据文件大小选择合适的单位显示
            long fileSize = file.length();
            if (fileSize < 1024 * 1024) { // 小于1MB的文件用KB显示
                System.out.printf("文件 '%s' 上传成功，大小: %.2f KB\n",
                        file.getName(), fileSize / 1024.0);
            } else { // 大于等于1MB的文件用MB显示
                System.out.printf("文件 '%s' 上传成功，大小: %.2f MB\n",
                        file.getName(), fileSize / (1024.0 * 1024.0));
            }

            // 生成并显示文件访问URL
            /*https://<bucket-name>.<endpoint-domain>/<object-key>*/
            //记得修改bucket和region
            String ossUrl = "https://" + bucket + ".oss-" + region + ".aliyuncs.com/" + key;
            System.out.printf("文件访问地址: %s\n", ossUrl);

            // 提示用户可以使用OSS控制台或SDK设置访问权限
            System.out.println("注意：文件访问权限取决于Bucket的访问策略设置。如需公开访问，请在OSS控制台设置对应的权限。")
            ;

        } catch (Exception e) {
            //If the exception is caused by ServiceException, detailed information can be obtained in this way.
            // ServiceException se = ServiceException.asCause(e);
            // if (se != null) {
            //    System.out.printf("ServiceException: requestId:%s, errorCode:%s\n", se.requestId(), se.errorCode());
            //}
            System.out.printf("error:\n%s", e);
        }
    }

    /**
     * 主方法，用于直接运行OSS文件上传功能
     * 使用示例：java uno.acloud.Example --region cn-hangzhou --bucket your-bucket --key remote-file.txt --file D:\\Users\\ZZC\\Downloads\\AccessKey (2).csv
     */
    public static void main(String[] args) {
        // 检查参数数量


        // 解析命令行参数
        String region = "cn-shenzhen";
        String bucket = "zxyz-database";
        String key = "iCANLab官方AI应用能力认证证书.png";
        String filePath = "D:\\Users\\ZZC\\Downloads\\iCANLab官方AI应用能力认证证书.png";
        String endpoint = null;// 默认域名


        // 验证文件是否存在
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println("错误: 文件不存在或不是有效的文件: " + filePath);
            return;
        }

        System.out.println("开始上传文件: " + filePath);
        System.out.println("目标: OSS://" + bucket + "/" + key);

        // 执行上传
        execute(endpoint, region, bucket, key, filePath);
    }
}