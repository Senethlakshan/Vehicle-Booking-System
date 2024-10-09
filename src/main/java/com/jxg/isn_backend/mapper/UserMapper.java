package com.jxg.isn_backend.mapper;

import com.jxg.isn_backend.dto.auth.UserMinDTO;
import com.jxg.isn_backend.model.Designation;
import com.jxg.isn_backend.model.Role;
import com.jxg.isn_backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "imageBlob", source = "imageBlob")
    @Mapping(target = "designation", source = "designation", qualifiedByName = "mapDesignationToString")
    @Mapping(target = "designationUrl", source = "designation", qualifiedByName = "mapDesignationUrl") // Map designation URL
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRoleToString") // Map role to string
    UserMinDTO toUserMinDTO(User user);


    //designation mapping
    @Named("mapDesignationToString")
    default String mapDesignationToString(Designation designation) {
        return designation != null ? designation.getName() : null;
    }

    @Named("mapDesignationUrl")
    default String mapDesignationUrl(Designation designation) {
        return designation != null ? designation.getImageUrl() : null;
    }

    @Named("mapRoleToString")
    default String mapRoleToString(Role role) {
        return role != null ? role.getAuthority().name() : null; // Get role as string
    }
}
