package com.petlog.petService.dto;

import com.petlog.petService.domain.Diary;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDiaryResponseDto extends Diary {

    @Nullable
    private String imgUrl;
}
