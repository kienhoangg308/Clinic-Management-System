package com.clinic.admin.customerservice;

import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.CustomerService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerServiceRepository extends PagingAndSortingRepository<CustomerService,Integer> {

//    public Ledger findByCustomerAndService(Customer customer, ClinicService service);
//
//    @Query("SELECT l FROM Ledger l WHERE l.customer.id = :customerId AND l.service.id = :serviceId AND l.remainingAmount > 0 ORDER BY l.createdAt ASC")
//    Optional<Ledger> findOldestWithRemainingAmount(@Param("customerId") Integer customerId, @Param("serviceId") Integer serviceId);

    @Query("SELECT c FROM CustomerService c WHERE c.customer = :customer AND c.service IN :services AND c.remainingAmount > 0 ORDER BY c.createdAt ASC")
    List<CustomerService> findOldestWithRemainingAmountForServices(@Param("customer") Customer customer, @Param("services") List<ClinicService> services);



}
