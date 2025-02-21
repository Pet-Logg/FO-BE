package com.petlog.petService.pet;

import com.petlog.petService.domain.Pets;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PetRepository {
    int createPetInfo(Pets pet);
}
