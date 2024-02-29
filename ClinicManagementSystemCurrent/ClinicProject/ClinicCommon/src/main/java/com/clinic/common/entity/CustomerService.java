package com.clinic.common.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer_service")
public class CustomerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ClinicService service;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

//    public CustomerService(){
//
//    }

    private int price;


    private int totalAmount;

    private int remainingAmount;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ClinicService getService() {
        return service;
    }


    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setService(ClinicService service) {
        this.service = service;
    }
}
