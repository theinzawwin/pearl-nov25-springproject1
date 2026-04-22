package com.pearl.nov25.springproj1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brands")
public class Brand implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "brand_name")
    private String name;

    private boolean status;
    @JsonIgnore
    @OneToMany(fetch =FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "brand")
    private List<Product> products=new ArrayList<>();

}
