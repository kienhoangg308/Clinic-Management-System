package com.clinic.admin.specialization;

import com.clinic.common.entity.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    public Specialization get(Integer specilizationId) {
        Specialization specialization = specializationRepository.findById(specilizationId).get();
        return specialization;

    }

    public Specialization findByName(String specializationName) {
        Specialization specialization = specializationRepository.findByName(specializationName);
        return specialization;
    }
}
