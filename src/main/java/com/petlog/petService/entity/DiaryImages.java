package com.petlog.petService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryImages {

    private int diaryImageId;
    private int diaryId;
    private String imgUrl;
}
