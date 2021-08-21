package nhom15.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.messages.Bucket;

@RestController
public class MinioStorageController {

	//link tham khao : https://www.programcreek.com/java-api-examples/?api=io.minio.MinioClient
	
	@Autowired
	    MinioAdapter minioAdapter;

	    @GetMapping(path = "/buckets")
	    public List<Bucket> listBuckets() {
	    	System.out.println("day la tat ca bucket"+ minioAdapter.getAllBuckets().get(0).name());
	        return minioAdapter.getAllBuckets();
	    }
//upload
	    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile[] files) throws IOException {
	       System.out.println("start upload...."); 
	       Map<String, String> result = new HashMap<>();
	       for (MultipartFile multipartFile : files) {
			
	       String fileName =   StringUtils.cleanPath(multipartFile.getOriginalFilename());

	     // Gui fileName va file sang ham upload
	    	minioAdapter.uploadFile(fileName, multipartFile );
	       
	        result.put(fileName, multipartFile.getOriginalFilename());
	        
	        }
	        return result;
	    }
 
//--------------------------------	    
//	test thu  ;http://localhost:8080/download/?url=phamvi.txt    
	    // download file : link cos thoi han 7 day start 08/08-2021
	    @GetMapping(path = "/download")
	    public ResponseEntity downloadFile(HttpServletResponse response, @RequestParam(value = "url") String file) throws IOException {
	        minioAdapter.getFile(file,response);

	        return ResponseEntity.ok("thanh cong");
	               
	                
	    }
	//----------------------------------------
	// API xoas

	  @GetMapping(path = "/deleteMinio")
	  public void delFileMinio(@RequestParam String name) {
		 
		  minioAdapter.remove(name);
		  
	  }
	    
	    
}
