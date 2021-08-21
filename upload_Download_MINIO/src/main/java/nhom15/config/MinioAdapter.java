package nhom15.config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.DeleteObjectTagsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;

@Service
public class MinioAdapter {
	 @Autowired
	 MinioClient minioClient;

	    @Value("${minio.buckek.name}")
	    String defaultBucketName;

	    @Value("${minio.default.folder}")
	    String defaultBaseFolder;

	    public List<Bucket> getAllBuckets() {
	        try {
	            return minioClient.listBuckets();
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }

	    }


	    public void uploadFile(String name, MultipartFile file) throws IOException {
	    	 System.out.println("start upload doc file....");
// url reference https://www.programcreek.com/java-api-examples/?api=io.minio.MinioClient
	    	//get content Type file
	   // String mimeType = ((File) file).toURL().openConnection().getContentType();	     	 
	        try {
	        	String uniqueID = UUID.randomUUID().toString();
	        	uniqueID+= name.substring((name.lastIndexOf(".")));
	            minioClient.putObject
	            (
	            		PutObjectArgs.builder()
	            		.bucket(defaultBucketName) // name buckget
	            		.object(defaultBaseFolder+"/"+uniqueID) // file se luu vao day
	            		.stream( new ByteArrayInputStream(file.getBytes()), file.getBytes().length, -1)
	            	    .contentType("application/octet-stream")
	            	    .build()
	            	        
	            );
	            System.out.println("path/to/ is created successfully");
	        } catch (Exception e) {
	           throw new RuntimeException(e.getMessage());
	        }

	    }

	    public void getFile(String key,HttpServletResponse response) {
	        try {
	            InputStream obj = minioClient.getObject
	            		(
	            				GetObjectArgs.builder()
	            				.bucket(defaultBucketName)
	            				.object(defaultBaseFolder+"/"+key) //path file can tai
	            				.build()
	            			
	            		);
	            response.setHeader("Content-Disposition", "attachment;filename=" + key);
	            response.setContentType("application/force-download");
	            response.setCharacterEncoding("UTF-8");
	            IOUtils.copy(obj, response.getOutputStream());
	           
	            obj.close();
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	    }

	    @PostConstruct
	    public void init() {
	    }


	    
		public void remove(String name) {
			// TODO Auto-generated method stub
			try {
				minioClient.removeObject(
						RemoveObjectArgs.builder()
						.bucket(defaultBucketName)
						.object(defaultBaseFolder+"/"+name)
						.build()
						);
				System.out.println(defaultBaseFolder+"/"+name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
}
