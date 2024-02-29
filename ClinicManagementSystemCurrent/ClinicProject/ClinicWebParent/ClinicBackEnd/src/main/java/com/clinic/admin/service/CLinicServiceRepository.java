package com.clinic.admin.service;

import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CLinicServiceRepository extends PagingAndSortingRepository<ClinicService, Integer> {

    public ClinicService findByName(String name);

    @Query("UPDATE ClinicService s SET s.enabled = ?2 WHERE s.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    public Long countById(Integer id);

    @Query("SELECT s FROM ClinicService s WHERE s.specialization = ?1")
    List<ClinicService> findBySpecialization(Specialization specialization);


    @Query("SELECT s FROM ClinicService s WHERE s.name LIKE %?1%")
    public Page<ClinicService> findAll(String keyword, Pageable pageable);

    @Query("SELECT cs FROM ClinicService cs WHERE cs.id IN :ids")
    List<ClinicService> findByIds(@Param("ids") List<Integer> ids);

}
