package com.sharmachait.shoppingcart.service.image;

import com.sharmachait.shoppingcart.dtos.ImageDto;
import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id) throws ResourceNotFoundException;
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> image, Long productId);
    void updateImage(MultipartFile image, Long imageId);
}
