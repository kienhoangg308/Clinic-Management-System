package com.clinic.admin.specialization;

import com.clinic.common.entity.Customer;
import com.clinic.common.entity.Specialization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends PagingAndSortingRepository<Specialization, Integer> {

    @Query("Select s FROM Specialization s WHERE s.name = :name")
    public Specialization findByName(@Param("name") String name);

}
