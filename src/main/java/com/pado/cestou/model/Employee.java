package com.pado.cestou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name="employees")
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

    @Enumerated(EnumType.STRING)
    @Column
    private Sector sector;

    @Enumerated(EnumType.STRING)
    @Column
    private WorkShift workShift;

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistration(int registration) {
        this.registration = registration;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public void setWorkShift(WorkShift workShift) {
        this.workShift = workShift;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRegistration() {
        return registration;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() { return password; }

    public Sector getSector() {
        return sector;
    }

    public WorkShift getWorkShift() {
        return workShift;
    }


}
