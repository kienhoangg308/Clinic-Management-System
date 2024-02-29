package com.clinic.common.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( unique = true, length = 45)
    private String email;

    @Column( length = 64)
    private String password;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column( length = 64)
    private String addressLine;

    @Column(length = 64)
    private String photos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;




    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private String gender;

    private boolean enabled;



    public Customer() {

    }

    public Customer(String password, String lastName, String phoneNumber,String email, String addressLine,String firstName) {
        this.password = password;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.addressLine = addressLine;
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName(){
        return lastName +" " +firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public String toString() {
        return "Customer [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

    @Transient
    public String getPhotosImagePath() {
        if (id == null || photos == null) return "/images/default-user.png";

        return "/user-photos/" + this.id + "/" + this.photos;
    }
}