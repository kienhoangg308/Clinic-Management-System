package com.clinic.common.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "services")
//public class ClinicService extends Auditable<String> {
public class ClinicService extends Audit<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 256, nullable = false)
    private String name;

    @Column(length = 128)
    private String image;

    private boolean enabled;


    private int price;

    private int totalAmount;


    private String alias;

    @Column(length = 512)
    private String description;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @ManyToMany(mappedBy = "services")
    private List<Appointment> appointments;

    @ManyToMany(mappedBy = "services")
    private List<Invoice> invoice;

    @ManyToMany(mappedBy = "services")
    private List<Record> records;


    public ClinicService() {
    }

    public ClinicService(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
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

    public List<Invoice> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<Invoice> invoice) {
        this.invoice = invoice;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Transient
    public String getImagePath() {
        if (this.id == null) return "/services/image-thumbnail.png";
        return "/service-images/" + this.id + "/" + this.image;
    }


}
