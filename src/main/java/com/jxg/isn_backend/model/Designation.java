package com.jxg.isn_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Designation extends AbstractAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String imageUrl;

    @JsonIgnore
//    @JsonIgnoreProperties("designation")
//    @OneToMany(mappedBy = "designation")
    @OneToMany(mappedBy = "designation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users;

//    @ManyToMany
//    Set<Department> departments;

}
