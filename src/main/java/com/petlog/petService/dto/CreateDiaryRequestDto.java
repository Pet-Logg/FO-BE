package com.petlog.petService.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateDiaryRequestDto {

    private int userId;
    private String title;
    private String content;
    private List<MultipartFile> images;

}
