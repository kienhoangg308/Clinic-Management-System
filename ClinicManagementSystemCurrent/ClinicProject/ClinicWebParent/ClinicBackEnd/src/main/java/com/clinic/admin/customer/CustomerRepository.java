package com.clinic.admin.customer;

import com.clinic.common.entity.Customer;
import com.clinic.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer,Integer> {


    @Query("Select c FROM Customer c WHERE c.email = :email")
    public Customer findByCustomerEmail(@Param("email") String email);

    public Long countById(Integer id);



    @Query("SELECT c FROM Customer c WHERE CONCAT(c.id, ' ', c.email, ' ', c.firstName, ' ',"
            + " c.lastName) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);


    @Query("Update Customer c SET c.enabled =?2 WHERE c.id=?1")
    @Modifying
    void updateCustomerStatus(Integer id, boolean enabled);
}
