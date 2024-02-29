package com.clinic.admin.appointment;

import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.Role;
import com.clinic.common.entity.Specialization;
import com.clinic.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, Integer> {


    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = ?1")
    Page<Appointment> findById(Integer id, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentCode LIKE CONCAT('%', ?1, '%') AND a.doctor.id = ?2")
    Page<Appointment> findByKeywordAndId(String keyword, Integer id, Pageable pageable);

//




    @Query("SELECT a FROM Appointment a WHERE str(a.id) LIKE %?1% OR a.doctor.firstName LIKE %?1% OR a.doctor.lastName LIKE %?1% OR a.customer.lastName LIKE %?1%OR a.customer.firstName LIKE %?1% OR str(a.status) LIKE %?1%")
    public Page<Appointment> findAll(String keyword, Pageable pageable);



    Long countById(Integer id);
}