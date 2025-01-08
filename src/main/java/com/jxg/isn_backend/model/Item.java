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
public class Item extends AbstractAuditable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_blob", referencedColumnName = "id")
    private FileBlob imageBlob;

    private String description;

    private String name;

    private Boolean isSold;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "item_tag",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;


    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subcategory;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
