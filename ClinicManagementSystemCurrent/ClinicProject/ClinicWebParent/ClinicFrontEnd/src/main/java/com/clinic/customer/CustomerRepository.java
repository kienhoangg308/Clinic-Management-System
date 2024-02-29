package com.clinic.customer;

import com.clinic.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email);

    @Query("Select c FROM Customer c WHERE c.email = :email")
    public Customer findByCustomerEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
    public Customer findByVerificationCode(String code);

    @Query("UPDATE Customer c SET c.enabled = true WHERE c.id = ?1")
    @Modifying
    public void enable(Integer id);
}
