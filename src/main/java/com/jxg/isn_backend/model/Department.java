package com.jxg.isn_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Department extends AbstractAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


//    @ManyToMany
//    @JoinTable(
//            name = "deparment_designation",
//            joinColumns = @JoinColumn(name = "department_id"),
//            inverseJoinColumns = @JoinColumn(name = "designation_id"))
//    Set<Designation> designations;


    @ManyToMany
    Set<Event> events;

    @ManyToMany
    Set<Branch> branches;


}
