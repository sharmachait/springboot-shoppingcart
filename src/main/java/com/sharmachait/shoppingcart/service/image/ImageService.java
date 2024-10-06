package com.sharmachait.shoppingcart.service.image;

import com.sharmachait.shoppingcart.dtos.ImageDto;
import com.sharmachait.shoppingcart.exceptions.ProductNotFoundException;
import com.sharmachait.shoppingcart.exceptions.ResourceNotFoundException;
import com.sharmachait.shoppingcart.model.Image;
import com.sharmachait.shoppingcart.model.Product;
import com.sharmachait.shoppingcart.repository.ImageRepository;
import com.sharmachait.shoppingcart.service.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Data
@Slf4j
public class ImageService implements IImageService {
    @Autowired
    private final ImageRepository imageRepository;
    @Autowired
    private final IProductService productService;
    @Override
    public Image getImageById(Long id) throws ResourceNotFoundException {
        return imageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        try {
            Image img = getImageById(id);
            imageRepository.delete(img);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @Transactional
    @Override
    public List<ImageDto> saveImage(List<MultipartFile> images, Long productId) {
        try {
            Product product = productService.getProductById(productId);
            List<ImageDto> imageDtos = new ArrayList<>();
            for (MultipartFile image : images) {
                try{
                    Image img = new Image();
                    img.setFilename(image.getOriginalFilename());
                    img.setFileType(image.getContentType());
                    img.setImage(new SerialBlob(image.getBytes()));
                    img = imageRepository.save(img);
                    img.setDownloadUrl("/image/" + img.getId());
                    img.setProduct(product);
//                    img = imageRepository.save(img);
                    imageDtos.add(mapImageToDto(img));
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    log.error(e.getMessage());
                    throw new RuntimeException("image could not be saved");
                }
            }
            return imageDtos;
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageDto mapImageToDto(Image img) {
        ImageDto dto = new ImageDto();
        dto.setImageName(img.getFilename());
        dto.setImageId(img.getId());
        dto.setDownloadUrl(img.getDownloadUrl());
        return dto;
    }

    @Override
    public void updateImage(MultipartFile image, Long imageId) {
        try {
            Image img = getImageById(imageId);
            img.setFilename(image.getOriginalFilename());
            img.setFileType(image.getContentType());
            img.setImage(new SerialBlob(image.getBytes()));
            img.setDownloadUrl("/image/" + imageId);
            imageRepository.save(img);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
