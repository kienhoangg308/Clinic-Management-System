package com.clinic.admin.service;

import com.clinic.admin.specialization.SpecializationRepository;

import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ClinicServiceService {

    public static final int SERVICES_PER_PAGE = 4;

    @Autowired
    private CLinicServiceRepository cLinicServiceRepository;

    @Autowired
    private SpecializationRepository specializationRepository;


    public List<ClinicService> listAll() {
        return (List<ClinicService>)cLinicServiceRepository.findAll();
    }

    public List<Specialization> listSpecializations() {
        return (List<Specialization>) specializationRepository.findAll();
    }

    public ClinicService save(ClinicService clinicService) {
        return cLinicServiceRepository.save(clinicService);
    }

    public ClinicService get(Integer id) throws ClinicServiceNotFoundException {
        try {
            return cLinicServiceRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ClinicServiceNotFoundException("Could not find any service with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);

        ClinicService serviceByName = cLinicServiceRepository.findByName(name);

        if (isCreatingNew) {
            if (serviceByName != null) {
                return "DuplicateName";
            } else {
                if (serviceByName != null && serviceByName.getId() != id) {
                    return "DuplicateName";
                }
            }
        }
        return "OK";
    }

    public void updateServiceEnabledStatus(Integer id, boolean enabled) {
        cLinicServiceRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws ClinicServiceNotFoundException {
        Long countById = cLinicServiceRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ClinicServiceNotFoundException("Could not find any service with ID " + id);
        }

        cLinicServiceRepository.deleteById(id);
    }

    public Page<ClinicService> listByPage(int pageNum, String sortDir, String sortField, String keyword) {

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, SERVICES_PER_PAGE, sort);

        if (keyword != null) {
            return cLinicServiceRepository.findAll(keyword, pageable);
        }

        return cLinicServiceRepository.findAll(pageable);
    }
}
