package com.petlog.petService.entity;

import com.petlog.common.entity.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diary extends CommonEntity {

    private int DiaryId;
    private int userId;
    private String title;
    private String content;

}
