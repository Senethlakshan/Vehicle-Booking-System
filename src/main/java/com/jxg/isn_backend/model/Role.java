package com.jxg.isn_backend.model;

import com.jxg.isn_backend.constant.RoleAuthorityEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleAuthorityEnum authority;

//    @JsonIgnore
//    @OneToMany(mappedBy = "role")
//    private Set<User> users;
}
