package com.clinic.common.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "record")
public class Record extends Audit<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToMany
    @JoinTable(
            name = "records_services",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ClinicService> services;

    private String diagnose;


    private String note;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<ClinicService> getServices() {
        return services;
    }

    public void setServices(List<ClinicService> services) {
        this.services = services;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }
}
