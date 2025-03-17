package com.petlog.petService.dto;

import com.petlog.petService.domain.Pets;
import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePetResponseDto extends Pets {

    @Nullable
    private List<String> disease = new ArrayList<>();
    @Nullable
    private List<String> allergy = new ArrayList<>();;

}
