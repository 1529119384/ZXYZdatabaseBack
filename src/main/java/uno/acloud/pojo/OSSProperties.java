package uno.acloud.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {
    private String region;
    private String bucket;
    private String endpoint;
}
