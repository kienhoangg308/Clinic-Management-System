package com.clinic.admin.appointment;

import com.clinic.admin.FileUploadUtil;
import com.clinic.admin.customer.CustomerNotFoundException;
import com.clinic.admin.customerservice.CustomerServiceService;
import com.clinic.admin.security.ClinicUserDetails;
import com.clinic.admin.service.ClinicServiceService;
import com.clinic.admin.specialization.SpecializationService;
import com.clinic.common.entity.*;
import com.clinic.common.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private ClinicServiceService clinicServiceService;

	@Autowired
	private CustomerServiceService customerServiceService;

	@Autowired
	private SpecializationService specializationService;

	@Autowired
	private com.clinic.admin.customer.CustomerService service;




	@GetMapping("/appointments")
	public String listFirstPage(Model model,@AuthenticationPrincipal ClinicUserDetails clinicUserDetails) {
		return listByPage(1,model, "bookingDate","asc", null, clinicUserDetails);
	}




	@GetMapping("/appointments/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
							 @Param("sortField") String sortField, @Param("sortDir") String sortDir,
							 @Param("keyword") String keyword, @AuthenticationPrincipal ClinicUserDetails loggedUser) {
//			if (sortDir ==  null || sortDir.isEmpty()) {
//				sortDir = "asc";
//			}
		Page<Appointment> page  = null;
		List<Appointment> listAppointments=new ArrayList<>();;
		System.out.println("Sort Field: " + sortField);
		System.out.println("Sort Order: " + sortDir);
		System.out.println("Page Num: " + pageNum);

		if(loggedUser.hasRole("Admin")){
			page = appointmentService.listByPage( pageNum, sortDir,sortField, keyword);
			System.out.println("role admin");
//		}else if(loggedUser.hasRole("Staff")){
//			page = service.listByPage(pageNum, sortDir,sortField, keyword);
		}else if(loggedUser.hasRole("Doctor")){
			page = appointmentService.listByIdAndPage(pageNum, sortDir,sortField, keyword,loggedUser.getUserId());
			System.out.println("docID: "+ loggedUser.getUserId());
			System.out.println("role doctor");
		}

//		Page<Appointment> page = service.listByPage( pageNum, sortDir,sortField, keyword);
		if(page != null){
			listAppointments = page.getContent();
		}else{
			System.out.println("can not find role ");
			return "index";
		}

		model.addAttribute("listAppointments", listAppointments);

		long startCount = (pageNum - 1) * AppointmentService.APPOINTMENT_PER_PAGE + 1;
		long endCount = startCount + AppointmentService.APPOINTMENT_PER_PAGE - 1;
//		if (endCount > page.getTotalElements()) {
//			endCount = page.getTotalElements();
//		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);


		model.addAttribute("reverseSortDir", reverseSortDir);

		return "appointments/appointments";
	}




	@GetMapping("/appointments/new")
	public String newAppointment(Model model) {
		List<Specialization> listSpecializations = appointmentService.listSpecializations();
		model.addAttribute("listSpecializations",listSpecializations);
		model.addAttribute("appointment", new Appointment());
		model.addAttribute("createNew", true);
		model.addAttribute("pageTitle", "Create New Appointment");

		return "appointments/appointment_form";
	}

	@PostMapping("/appointments/save")
	public String saveAppointment(Appointment appointment,
							   RedirectAttributes ra) throws IOException {
		String email = appointment.getCustomer().getEmail();
		String specializationName = appointment.getSpecialization().getName();
		Specialization specialization = specializationService.findByName(specializationName);
		Customer customer = appointmentService.findByCustomerEmail(email);
		List<ClinicService> services = appointment.getServices();
		appointment.setSpecialization(specialization);
		appointment.setServices(services);
		appointment.setCustomer(customer);
		appointmentService.save(appointment);

		ra.addFlashAttribute("message", "The appointment has been saved successfully.");
		return getRedirectURLtoAffectedAppointment(appointment.getId());
	}


	@GetMapping("/appointments/edit/{id}")
	public String editAppointment(@PathVariable(name = "id") Integer id, Model model,
								  RedirectAttributes ra) {
		try {
			Appointment appointment = appointmentService.get(id);
			Specialization specialization = appointment.getSpecialization();

			List<Specialization> listSpecializations = appointmentService.listSpecializations();
			List<ClinicService> listServices = specialization.getClinicServices();
			List<User> listDoctors = specialization.getDoctor();

			model.addAttribute("listServices",listServices);
			model.addAttribute("listDoctors",listDoctors);

			model.addAttribute("listSpecializations",listSpecializations);
			model.addAttribute("appointment", appointment);
			model.addAttribute("pageTitle", "Edit Appointment (ID: " + id + ")");

			return "appointments/appointment_update_form";
		} catch (AppointmentNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/appointments";
		}
	}


	@GetMapping("/appointments/delete/{id}")
	public String deleteAppointment(@PathVariable(name = "id") Integer id,
								 Model model,
								 RedirectAttributes redirectAttributes) {
		try {
			appointmentService.delete(id);

			redirectAttributes.addFlashAttribute("message",
					"The appointment ID " + id + " has been deleted successfully");
		} catch (AppointmentNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/appointments";
	}


	@GetMapping("/appointments/{appointmentId}/approve")
	public String approveAppointment(@PathVariable("appointmentId") int appointmentId) {
		appointmentService.approveAppointment(appointmentId);
		return getRedirectURLtoAffectedAppointment(appointmentId);
	}

	private String getRedirectURLtoAffectedAppointment(Integer appointmentId) {
		return "redirect:/appointments/page/1?sortField=id&sortDir=asc&keyword=" + appointmentId;
	}

	@GetMapping("/appointments/{appointmentId}/cancel")
	public String cancelAppointment(@PathVariable("appointmentId") int appointmentId) {
		appointmentService.cancelAppointment(appointmentId);
		return "redirect:/appointments";
	}

	@GetMapping("/appointments/{appointmentId}/customerservice/new")
	public String newCustomerService(@PathVariable(name = "appointmentId") Integer appointmentId,
									 Model model) throws AppointmentNotFoundException {  // modal dialog to display service
		Appointment appointment = appointmentService.get(appointmentId);
		Customer customer = appointment.getCustomer();
		Specialization specialization = appointment.getSpecialization();
		List<ClinicService> listServices = appointmentService.findBySpecialization(specialization);
		//List<ClinicService> listServices = clinicServiceService.listAll();
		System.out.println(customer.getEmail());
		model.addAttribute("listServices", listServices);
		model.addAttribute("customerService", new CustomerService());
		model.addAttribute("customer", customer);
		model.addAttribute("appointment", appointment);
		//model.addAttribute("pageTitle", "Add service for customer");

		return "service_selection/invoice_form";

	}

	@GetMapping("/appointments/{appointmentId}/customerservice/create")
	public String createInvoice(@PathVariable(name = "appointmentId") Integer appointmentId,
									  Model model) throws AppointmentNotFoundException {  //create invoice form
		Appointment appointment = appointmentService.get(appointmentId);
		Integer totalPrice = appointmentService.calculateTotalPrice(appointment.getCustomerServices());
		appointment.setTotalPrice(totalPrice);
		appointmentService.savePriceForAppointment(appointment);
		System.out.println("total price for check: " + appointment.getTotalPrice());
		model.addAttribute("totalPrice", totalPrice);

		model.addAttribute("appointment", appointment);
		model.addAttribute("pageTitle", "View invoice");

		return "service_selection/invoice2";

	}

	@GetMapping("/appointments/{appointmentId}/customerservice/view")
	public String viewInvoice(@PathVariable(name = "appointmentId") Integer appointmentId,
								Model model) throws AppointmentNotFoundException {  //view invoice form
		Appointment appointment = appointmentService.get(appointmentId);
		Integer totalPrice = appointmentService.calculateTotalPrice(appointment.getCustomerServices());
		//appointment.setTotalPrice(totalPrice);
		System.out.println("total price: " + appointment.getTotalPrice());
		model.addAttribute("totalPrice", totalPrice);

		model.addAttribute("appointment", appointment);
		model.addAttribute("pageTitle", "View invoice");

		return "service_selection/invoice3";

	}



}