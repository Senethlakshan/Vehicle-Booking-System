package com.jxg.isn_backend.mapper;

import com.jxg.isn_backend.dto.response.SubCategoryResponseDTO;
import com.jxg.isn_backend.model.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubcategoryMapper {

    SubcategoryMapper INSTANCE = Mappers.getMapper(SubcategoryMapper.class);

    // Mapping SubCategory to SubCategoryResponseDTO
    SubCategoryResponseDTO toSubCategoryResponseDTO(SubCategory subCategory);

    // Optional: Mapping SubCategoryResponseDTO back to SubCategory if needed
    SubCategory toSubCategory(SubCategoryResponseDTO subCategoryResponseDTO);
}
