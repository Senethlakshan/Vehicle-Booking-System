package com.jxg.isn_backend.repository;

import com.jxg.isn_backend.constant.RoleAuthorityEnum;
import com.jxg.isn_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByAuthority(RoleAuthorityEnum authority);
}
