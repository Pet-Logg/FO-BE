package com.petlog.productService.product;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.petlog.config.S3Config;
import com.petlog.productService.dto.CreateProductDto;
import com.petlog.productService.entity.ProductImages;
import com.petlog.productService.entity.Products;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Config s3Config;
    private final AmazonS3 amazonS3;

    public void createProduct(CreateProductDto dto, int userId) {

        Products product = new Products();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setUserId(userId);

        productRepository.createProduct(product);

        int productId = product.getProductId(); // 자동생성된 다이어리 Id 가져오기

        ProductImages productImg = new ProductImages();
        productImg.setProductId(productId);

        // 이미지 업로드 (서버 저장 후 DB에 URL 저장)
        List<MultipartFile> images = dto.getProductImg();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imgUrl = uploadFile(image); // 실제 이미지 저장 후 URL 반환
                productImg.setImgUrl(imgUrl);
                productRepository.insertProductImage(productImg);
            }
        }
    }

    public List<Products> getAllProducts() {
        return productRepository.getAllProducts();
    }
//
//
//    public void updateStock(CreateOrderDto createOrderDto) {
//        log.info("Starting updateStock for productId: " + createOrderDto.getProductId());
//        Product product = productRepository.findById(createOrderDto.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        int newStock = product.getQuantity() - createOrderDto.getQuantity();
//        if (newStock < 0) {
//            log.error("Not enough stock available for productId: " + createOrderDto.getProductId());
//            throw new RuntimeException("Not enough stock available");
//        }
//        product.setQuantity(newStock);
//        product.setUpdatedAt(LocalDateTime.now());
//        productRepository.save(product);
//        log.info("Finished updateStock for productId: " + createOrderDto.getProductId());
//    }
//
//    public void restoreStock(CreateOrderDto createOrderDto) {
//        Product product = productRepository.findById(createOrderDto.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        int newStock = product.getQuantity() + createOrderDto.getQuantity();
//        if (newStock < 0) {
//            throw new RuntimeException("Not enough stock available");
//        }
//        product.setQuantity(newStock);
//        product.setUpdatedAt(LocalDateTime.now());
//        productRepository.save(product);
//    }
//
//    public int getProductPrice(int productId) {
//        log.info("Starting getProductPrice for productId: " + productId);
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        int price = product.getPrice();
//        log.info("Finished getProductPrice for productId: " + productId + ", price: " + price);
//        return price;
//    }
//    public List<Product> getProductsByIds(List<Integer> productIds) {
//        return productRepository.findAllById(productIds);
//    }
//

//
//    public Product updateProduct(int id, PutProductDto putProductDto) {
//        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
//        product.setName(putProductDto.getName());
//        product.setPrice(putProductDto.getPrice());
//        product.setDescription(putProductDto.getDescription());
//        product.setExposeYsno(putProductDto.getExposeYsno());
//        return productRepository.save(product);
//    }
//
//    public void deleteProduct(int id) {
//        productRepository.deleteById(id);
//    }

    public String uploadFile(MultipartFile file){
        String bucketName = s3Config.getS3().getBucket();
        String fileName = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucketName,fileName).toString(); // 업로드된 파일 URL 반환

        } catch (IOException e){
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }
}
