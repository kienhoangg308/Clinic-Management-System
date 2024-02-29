package com.clinic.common.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appointments")
public class Appointment extends Audit<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true)
    private String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;

    @Column(length = 512)
    private String note;

//    @Column(length = 120)
//    private String diagnose;

    private boolean enabled;

    private String appointmentCode;

    @ManyToOne(fetch = FetchType.EAGER)   //cascade = CascadeType.ALL
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne(fetch = FetchType.EAGER)  //cascade = CascadeType.ALL
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerService> customerServices = new ArrayList<>();

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    private Integer totalPrice;


    @ManyToMany
    @JoinTable(
    name = "appointments_services",
    joinColumns = @JoinColumn(name = "appointment_id"),
    inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ClinicService> services;

    public Appointment() {
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAppointmentCode() {
        return appointmentCode;
    }

    public void setAppointmentCode(String appointmentCode) {
        this.appointmentCode = appointmentCode;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }


    public List<ClinicService> getServices() {
        return services;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setServices(List<ClinicService> services) {
        this.services = services;
    }

    public List<CustomerService> getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(List<CustomerService> customerServices) {
        this.customerServices = customerServices;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
