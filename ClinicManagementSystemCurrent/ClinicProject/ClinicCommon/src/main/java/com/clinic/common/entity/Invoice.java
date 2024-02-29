package com.clinic.common.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "invoices_services",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ClinicService> services;




    public Invoice() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ClinicService> getServices() {
        return services;
    }

    public void setServices(List<ClinicService> services) {
        this.services = services;
    }
}
