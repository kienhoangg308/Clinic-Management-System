package com.clinic.admin.record;

import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Record;
import com.clinic.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecordRepository extends PagingAndSortingRepository<Record,Integer> {


//    @Query("SELECT r FROM Record r WHERE r.appointment.customer.firstName LIKE %?1%")
//    public Page<Record> findAll(String keyword, Pageable pageable);

    @Query("SELECT r FROM Record r WHERE CONCAT(r.id, ' ', r.appointment.customer.firstName, ' ', r.appointment.doctor.firstName) LIKE %?1%")
    public Page<Record> findAll(String keyword, Pageable pageable);

}
