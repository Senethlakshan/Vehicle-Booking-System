//package com.jxg.isn_backend.dto.request;
//
//import com.jxg.isn_backend.model.Category;
//import com.jxg.isn_backend.model.SubCategory;
//import com.jxg.isn_backend.model.Tag;
//
//import java.util.Set;
//
//public record CreateItemRequestDTO(
//        String description,
//        String name,
//        Boolean isSold,
//        Set<Tag> tags,
//        SubCategory subCategory,
//        Category category
//) {
//}
package com.jxg.isn_backend.dto.request;

import com.jxg.isn_backend.model.Category;
import com.jxg.isn_backend.model.SubCategory;
import com.jxg.isn_backend.model.Tag;

import java.util.Set;

public record CreateItemRequestDTO(
        Long id,
        String description,
        String name,
        Boolean isSold,
        Set<Tag> tags,
        SubCategory subCategory,
        Category category
) {
}