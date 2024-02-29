package com.clinic;

import com.clinic.common.entity.ClinicService;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicServiceRepository extends PagingAndSortingRepository<ClinicService, Integer> {

}
