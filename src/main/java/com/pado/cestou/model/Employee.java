package com.pado.cestou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="employees")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private int registration;

    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column
    private Sector sector;

    @Enumerated(EnumType.STRING)
    @Column
    private WorkShift workShift;

}
