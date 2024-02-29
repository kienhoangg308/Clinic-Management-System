package com.clinic.common.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "specializations")
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false, unique = true)
    private String name;

    @Column(length = 150)
    private String description;


    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicService> clinicServices = new ArrayList<>();

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> doctor = new ArrayList<>();



    public Specialization() {
    }

    public Specialization(Integer id) {
        this.id = id;
    }

    public Specialization(String name) {
        this.name = name;
    }



    public Specialization(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getDoctor() {
        return doctor;
    }

    public void setDoctor(List<User> doctor) {
        this.doctor = doctor;
    }

    public List<ClinicService> getClinicServices() {
        return clinicServices;
    }

    public void setClinicServices(List<ClinicService> clinicServices) {
        this.clinicServices = clinicServices;
    }

    @Override
    public String toString() {
        return  name.toString() ;
    }
}
