package com.clinic;
import org.springframework.core.convert.converter.Converter;

import com.clinic.common.entity.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringToClinicServiceConverter implements Converter<String, ClinicService> {

    @Autowired
    private ClinicServiceRepository clinicServiceRepository;

    @Override
    public ClinicService convert(String source) {
        Integer id = Integer.valueOf(source);
        return clinicServiceRepository.findById(id).orElse(null); // replace with your method
    }
}