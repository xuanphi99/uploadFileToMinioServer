package nhom15.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MiniConfig {

	@Value("${minio.access.name}")
    String accessKey;
	
    @Value("${minio.access.secret}")
    String accessSecret;
    
    @Value("${minio.url}")
    String minioUrl;
    
    @Bean
    public MinioClient generateMinioClient() {
        try {
            return MinioClient.builder()
            		.endpoint(minioUrl,9000,false)
            		.credentials(accessKey, accessSecret)
            		.build();
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    
}
