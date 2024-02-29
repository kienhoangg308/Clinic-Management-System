package com.clinic.appointment;

import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.Specialization;
import com.clinic.security.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/appointment")
    public String newAppointment(Model model) {
        List<Specialization> listSpecializations = appointmentService.listSpecializations();
        model.addAttribute("listSpecializations",listSpecializations);
        model.addAttribute("appointment", new Appointment());
//        model.addAttribute("createNew", true);
        model.addAttribute("pageTitle", "Book Appointment");

        return "appointment/appointment_form";
    }

    @PostMapping("/appointment/save")
    public String saveAppointment(@ModelAttribute Appointment appointment,RedirectAttributes redirectAttributes,
                                  RedirectAttributes ra,@AuthenticationPrincipal CustomerUserDetails loggedUser) throws IOException {
//		if (!multipartFile.isEmpty()) {
//			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			appointment.setImage(fileName);
//
//			Category savedCategory = service.save(category);
//			String uploadDir = "../category-images/" + savedCategory.getId();
//
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//		} else {
//			service.save(category);
//		}
        if (loggedUser == null) {
            redirectAttributes.addFlashAttribute("message", "You must be logged in to book an appointment.");
            return "redirect:/login"; // Redirect to the login page or an error page
        }
        System.out.println("Hello");

        String email = loggedUser.getUsername();
        String specializationName = appointment.getSpecialization().getName();
        Specialization specialization = appointmentService.findSpecializationByName(specializationName);
        Customer customer = appointmentService.findByCustomerEmail(email);
        List<ClinicService> services = appointment.getServices();
        for(ClinicService service : services){
            System.out.println(service.getName());
        }
        appointment.setSpecialization(specialization);
        appointment.setServices(services);
        appointment.setCustomer(customer);
        appointmentService.save(appointment);

        ra.addFlashAttribute("message", "The appointment has been saved successfully.");
        return "redirect:/";
    }

//    @PostMapping("/appointment/save")
//    public String saveAppointment(Appointment appointment,
//                                  RedirectAttributes ra) throws IOException {
////		if (!multipartFile.isEmpty()) {
////			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
////			appointment.setImage(fileName);
////
////			Category savedCategory = service.save(category);
////			String uploadDir = "../category-images/" + savedCategory.getId();
////
////			FileUploadUtil.cleanDir(uploadDir);
////			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
////		} else {
////			service.save(category);
////		}
//        String email = appointment.getCustomer().getEmail();
//        String specializationName = appointment.getSpecialization().getName();
//        Specialization specialization = appointmentService.findSpecializationByName(specializationName);
//        Customer customer = appointmentService.findByCustomerEmail(email);
//        List<ClinicService> services = appointment.getServices();
//        appointment.setSpecialization(specialization);
//        appointment.setServices(services);
//        appointment.setCustomer(customer);
//        appointmentService.save(appointment);
//
//        ra.addFlashAttribute("message", "The appointment has been saved successfully.");
//        return "redirect:/";
//    }
}
