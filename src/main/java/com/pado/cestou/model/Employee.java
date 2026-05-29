package com.pado.cestou.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.lang.annotation.RequiredTypes;

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

    @Column(unique = true)
    private String cellNumber;

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

    public void setCellNumber(String cellNumber) { this.cellNumber = cellNumber; }

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

    public String getCellNumber() { return cellNumber; }

    public Sector getSector() {
        return sector;
    }

    public WorkShift getWorkShift() {
        return workShift;
    }

}
