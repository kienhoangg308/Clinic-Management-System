package com.clinic.admin.specialization;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specialization not found")
public class SpecializationNotFoundRestException extends Throwable {
}
