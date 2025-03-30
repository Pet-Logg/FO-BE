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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Config s3Config;
    private final AmazonS3 amazonS3;

    public void createProduct(CreateProductRequestDto dto, int userId) {

        Products product = new Products();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setUserId(userId);

        productRepository.createProduct(product);

        int productId = product.getProductId(); // 자동생성된 다이어리 Id 가져오기

        ProductImages productImg = new ProductImages();
        productImg.setProductId(productId);

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
        productRepository.insertProductImage(imageEntities);

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

        List<String> oldKeys = productRepository.findS3KeysByProductId(productId);

        // S3에 이미지 삭제
        deleteFiles(oldKeys);
        // DB에 이미지 삭제
        productRepository.deleteImgByProductId(productId);
    }

    @Transactional
    public void updateProduct(int productId, UpdateProductRequestDto dto) {

        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("name", dto.getName());
        params.put("price",dto.getPrice());
        params.put("quantity", dto.getQuantity());

        // 1. 상품 업데이트
        productRepository.updateProduct(params);

        // 2. 모든 기존 이미지 S3 key 조회
        List<String> existingKeys = productRepository.findS3KeysByProductId(productId);

        // 3. 삭제 대상 필터링
        List<String> toDeleteS3Keys = new ArrayList<>();
        for (String key : existingKeys) {
            if (!dto.getS3Key().contains(key)) {
                toDeleteS3Keys.add(key);
            }
        }

        // 4. 삭제 처리
        if (!toDeleteS3Keys.isEmpty()) {
            deleteFiles(toDeleteS3Keys);
            productRepository.deleteImgByS3Keys(toDeleteS3Keys); // key 기반 삭제
        }

        // 5. 새 이미지 업로드 및 DB 저장
        if (dto.getProductImg() != null && !dto.getProductImg().isEmpty()) {
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

            if (!imageEntities.isEmpty()) {
                productRepository.insertProductImage(imageEntities);
            }
        }
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

    public void deleteWishList(DeleteWishListRequestDto dto, int userId){
        productRepository.deleteWishList(dto, userId);
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
