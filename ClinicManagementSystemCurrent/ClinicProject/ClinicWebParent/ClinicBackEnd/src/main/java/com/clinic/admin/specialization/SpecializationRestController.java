package com.clinic.admin.specialization;

import com.clinic.admin.service.ServiceDTO;
import com.clinic.admin.user.DoctorDTO;
import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Specialization;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.management.ServiceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class SpecializationRestController {

    @Autowired
    private SpecializationService specializationService;


    @GetMapping("/specializations/{id}/services")
    public List<ServiceDTO> listServicesBySpecialization(@PathVariable(name = "id") Integer specilizationId) throws SpecializationNotFoundRestException {
        List<ServiceDTO> listClinicServices = new ArrayList<>();

        Specialization specialization = specializationService.get(specilizationId);
        List<ClinicService> clinicServices = specialization.getClinicServices();

        for (ClinicService clinicService : clinicServices) {
            ServiceDTO dto = new ServiceDTO(clinicService.getId(), clinicService.getName());
            System.out.println(clinicService.getName());
            listClinicServices.add(dto);
        }

        return listClinicServices;
    }

    @GetMapping("/specializations/{id}/doctors")
    public List<DoctorDTO> listDoctorsBySpecialization(@PathVariable(name = "id") Integer specilizationId) {
        List<DoctorDTO> listDoctor = new ArrayList<>();

        Specialization specialization = specializationService.get(specilizationId);
        List<User> doctors = specialization.getDoctor();

        for (User doctor : doctors) {
            DoctorDTO dto = new DoctorDTO(doctor.getId(), doctor.getLastName() +" "+ doctor.getFirstName());
            listDoctor.add(dto);
        }

        return listDoctor;
    }
}
