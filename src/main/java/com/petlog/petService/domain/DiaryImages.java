package com.petlog.petService.domain;

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
