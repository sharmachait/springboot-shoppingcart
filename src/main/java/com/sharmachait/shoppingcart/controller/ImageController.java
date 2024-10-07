package com.sharmachait.shoppingcart.controller;

import com.sharmachait.shoppingcart.dtos.ApiResponse;
import com.sharmachait.shoppingcart.dtos.ImageDto;
import com.sharmachait.shoppingcart.model.Image;
import com.sharmachait.shoppingcart.service.image.IImageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.List;

@Data
@RestController
@RequestMapping("${api.prefix}/images")
@Slf4j
public class ImageController {
    @Autowired
    private final IImageService imageService;

    @PostMapping("/upload") // save?productId = 123123
    public ResponseEntity<ApiResponse> saveImages(
            @RequestBody  List<MultipartFile> images, @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImages(images, productId);
            return ResponseEntity.ok(new ApiResponse("Uploaded successfully!", imageDtos));
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong!", e.getMessage()));
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<ApiResponse> downloadImage(@PathVariable Long imageId) {
        try{
            Image image= imageService.getImageById(imageId);
            Blob imageBlob =  image.getImage();
            ByteArrayResource resource = new ByteArrayResource(imageBlob.getBytes(1, (int) imageBlob.length()));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+image.getFilename() +"\"")
                    .body(new ApiResponse("Image sent!",resource));
        }catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong!", e.getMessage()));
        }
    }
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile imageUpdate) {
        try{
            Image image = imageService.getImageById(imageId);
            imageService.updateImage(imageUpdate, imageId);
            return ResponseEntity.ok(new ApiResponse("Updated successfully!", null));
        }catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try{
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Deleted successfully!", null));
        }catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
