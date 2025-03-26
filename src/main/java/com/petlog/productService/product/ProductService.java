package com.petlog.productService.product;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.petlog.config.S3Config;
import com.petlog.productService.dto.*;
import com.petlog.productService.entity.ProductImages;
import com.petlog.productService.entity.Products;
import com.petlog.productService.entity.WishLists;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
//        List<MultipartFile> imageEntities = dto.getProductImg();
//        if (imageEntities != null && !imageEntities.isEmpty()) {
//            for (MultipartFile image : imageEntities) {
//                String imgUrl = uploadFile(image); // 실제 이미지 저장 후 URL 반환
//                productImg.setImgUrl(imgUrl);
//                productRepository.insertProductImage(productImg);
//            }
//        }

        // 3. 새 이미지 업로드 및 DB 저장
        List<ProductImages> imageEntities = new ArrayList<>();
        for (MultipartFile file : dto.getProductImg()) {
            String s3Key = generateS3Key(productId, file.getOriginalFilename());
            String url = uploadFileToS3(file, s3Key);

            ProductImages image = new ProductImages();
            image.setProductId(productId);
            image.setImgUrl(url);
            image.setS3Key(s3Key);

            imageEntities.add(image);
        }
        productRepository.insertProductImage2(imageEntities);

    }

    public List<GetProductsResponseDto> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public GetProductsResponseDto getProductById(int productId) {
        return productRepository.getProductById(productId);
    }

    @Transactional
    public void deleteProduct(int productId) {
        productRepository.deleteProduct(productId);
    }

    public void updateProduct(int productId, CreateProductDto dto) {

        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("name", dto.getName());
        params.put("price",dto.getPrice());
        params.put("quantity", dto.getQuantity());

        // 1. 상품 업데이트
        productRepository.updateProduct(params);

        // 2. 기존 이미지 S3 삭제 및 DB 삭제
        List<String> oldKeys = productRepository.findS3KeysByProductId(productId);
        deleteFiles(oldKeys);
        productRepository.deleteImgByProductId(productId);

        // 3. 새 이미지 업로드 및 DB 저장
        List<ProductImages> imageEntities = new ArrayList<>();
            for (MultipartFile file : dto.getProductImg()) {
                String s3Key = generateS3Key(productId, file.getOriginalFilename());
                String url = uploadFileToS3(file, s3Key);

                ProductImages image = new ProductImages();
                image.setProductId(productId);
                image.setImgUrl(url);
                image.setS3Key(s3Key);

                imageEntities.add(image);
            }
        productRepository.insertProductImage2(imageEntities);
    }


    public void addWishList(int userId, CartItemRequestDto dto){
        WishLists wishList = new WishLists();

        wishList.setUserId(userId);
        wishList.setProductId(dto.getProductId());
        wishList.setQuantity(dto.getQuantity());

        productRepository.addWishList(wishList);
    }

    public List<GetWishListResponseDto> getWishList(int userId){
         return productRepository.getWishList(userId);
    }

    public void updateWishList(CartItemRequestDto dto, int userId){
        productRepository.updateWishList(dto, userId);
    }

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

    public String uploadFileToS3(MultipartFile file, String s3Key) {
        String bucketName = s3Config.getS3().getBucket();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucketName, s3Key).toString(); // 업로드된 파일 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    public String generateS3Key(int productId, String originalFilename) {
        return "uploads/products/" + productId + "/" + UUID.randomUUID() + "_" + originalFilename;
    }


    public void deleteFiles(List<String> s3Keys) {
        String bucketName = s3Config.getS3().getBucket();

        if (s3Keys.isEmpty()) return;

        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName)
                .withKeys(s3Keys.toArray(new String[0]));

        amazonS3.deleteObjects(request);
    }

}
