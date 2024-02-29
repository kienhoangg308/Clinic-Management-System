package com.clinic.common.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, unique = true)
    private String email;

    @Column(length = 64)
    private String password;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(length = 64)
    private String photos;


    private Date dob;

    @Column(name = "address")
    private String address;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    private String degree;

    private boolean enabled;

    public User(){

    }

    public User(String userName, String email, String password, String firstName, String lastName, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.userName = userName;

    }

    public User(String email, String password, String firstName, String lastName, String photos, Date dob, String address, String code, String gender, String phoneNumber, Role role, boolean enabled) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photos = photos;
        this.dob = dob;
        this.address = address;
        this.code = code;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.enabled = enabled;
    }

    public boolean hasRole(String roleName) {
        if (role.getName().equals(roleName)) {
            return true;
        }

        return false;
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

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }


    public void generateCode(Role userRole){
        System.out.println("hello");
        if(userRole.getName().equals("Doctor")){
            this.code = "DOC-" + UUID.randomUUID().toString();
            System.out.println("role doctor");
        }
        if(userRole.getName().equals("Staff")){
            this.code = "STAFF-" + UUID.randomUUID().toString();
            System.out.println("staff");
        }
        if(userRole.getName().equals("Customer")){
            this.code = "PAT-" + UUID.randomUUID().toString();
            System.out.println("customer");
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean status) {
        this.enabled = status;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    //    public void addRole(Role role){
//        this.role.add(role);
//    }

    public String getName(){
        return lastName +" " +firstName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photos='" + photos + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", code='" + code + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", status=" + enabled +
                '}';
    }

    @Transient
    public String getPhotosImagePath() {
        if (id == null || photos == null) return "/images/default-user.png";

        return "/user-photos/" + this.id + "/" + this.photos;
    }



}
