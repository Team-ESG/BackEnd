package esgback.esg.Controller.File;

import com.amazonaws.services.s3.AmazonS3Client;
import esgback.esg.DTO.Response;
import esgback.esg.Util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3Client amazonS3Client;
    private final S3Util s3Util;
    private final Response response;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/auth/upload/market")
    public ResponseEntity<?> uploadMarket(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("authorization") String authorization) {
        try {
            String url = s3Util.uploadMarketImg(multipartFile, authorization);

            return response.success(url, "success", HttpStatus.OK);
        } catch (Exception e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/auth/upload/item")
    public ResponseEntity<?> uploadItem(@RequestParam("file") MultipartFile multipartFile){
        try {
            String url = s3Util.uploadItemImg(multipartFile);

            return response.success(url, "success", HttpStatus.OK);
        } catch (Exception e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
