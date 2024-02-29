package com.clinic.admin.appointment;

import com.clinic.admin.customer.CustomerRepository;
import com.clinic.admin.service.CLinicServiceRepository;
import com.clinic.admin.service.ServiceDTO;
import com.clinic.admin.specialization.SpecializationRepository;
import com.clinic.admin.specialization.SpecializationService;
import com.clinic.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

	public static final int APPOINTMENT_PER_PAGE = 4;


	@Autowired
	private AppointmentRepository AppointmentRepository;

	@Autowired
	private SpecializationRepository SpecializationRepository;

	@Autowired
	private CLinicServiceRepository cLinicServiceRepository;

	@Autowired
	private CustomerRepository CustomerRepository;

	@Autowired
	private SpecializationService specializationService;


	public Customer findByCustomerEmail(String email) {
		Customer customer = CustomerRepository.findByCustomerEmail(email);
		return customer;
	}

	public List<Appointment> listAppointments() {
		return (List<Appointment>) AppointmentRepository.findAll();
	}

	public List<Specialization> listSpecializations() {
		return (List<Specialization>) SpecializationRepository.findAll();
	}



//	public Appointment save(Appointment appointment){
//		return AppointmentRepository.save(appointment);
//	}

	public Appointment save(Appointment appointment){
		appointment.setStatus(AppointmentStatus.PENDING);
		return AppointmentRepository.save(appointment);
	}

	public Appointment get(Integer id) throws AppointmentNotFoundException {
		try {
			return AppointmentRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new AppointmentNotFoundException("Could not find any appointment with ID " + id);
		}
	}


	public void delete(Integer id) throws AppointmentNotFoundException {
		Long countById = AppointmentRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new AppointmentNotFoundException("Could not find any appointment with ID " + id);
		}

		AppointmentRepository.deleteById(id);
	}

	public Page<Appointment> listByPage(int pageNum, String sortDir,String sortField, String keyword) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, APPOINTMENT_PER_PAGE, sort);

		if (keyword != null) {
			return AppointmentRepository.findAll(keyword, pageable);
		}

		return AppointmentRepository.findAll(pageable);
	}

	public Page<Appointment> listByIdAndPage(int pageNum, String sortDir,String sortField, String keyword, Integer id) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, APPOINTMENT_PER_PAGE, sort);

		if (keyword != null) {
			return AppointmentRepository.findByKeywordAndId(keyword, id, pageable);
		}

		return AppointmentRepository.findById(id, pageable);
	}


	private List<Integer> findItemOfSourceNotTarget(List<Integer> source, List<Integer> target) {
		List<Integer> itemOfSourceButNotTarget = new ArrayList<>();
		for(int i =0;i<source.size();i++) {
			if(!target.contains(source.get(i))) {
				itemOfSourceButNotTarget.add(source.get(i));
			}
		}
		return itemOfSourceButNotTarget;
	}



    public List<ServiceDTO> loadService(Integer appointmentId, Integer specializationId) {

		List<ServiceDTO> serviceDTOS = new ArrayList<>();
		Specialization specialization = specializationService.get(specializationId);
		List<ClinicService> clinicServices = specialization.getClinicServices();
		Appointment appointment = AppointmentRepository.findById(appointmentId).get();

		List<Integer> serviceIds = clinicServices.stream()           //serviceIds  ->  tat ca service cua 1 category
				.map(ClinicService::getId)
				.collect(Collectors.toList());

		List<ClinicService> services = appointment.getServices();    // appointment.getService()
		List<Integer> assignedServiceIds = services.stream()   //-> service cua appointment
				.map(ClinicService::getId)
				.collect(Collectors.toList());

		List<Integer> uncheckedServiceIds = findItemOfSourceNotTarget(serviceIds, assignedServiceIds);


		serviceDTOS = clinicServices.stream()
				.map(clinicService -> {
					ServiceDTO serviceDTO = new ServiceDTO();
					serviceDTO.setName(clinicService.getName());
					serviceDTO.setId(clinicService.getId());
					if(assignedServiceIds.contains(clinicService.getId())){
						serviceDTO.setChecked("checked");
					}else if (uncheckedServiceIds.contains(clinicService.getId())) {
						serviceDTO.setChecked("unchecked");
					}
					return serviceDTO;
				})
				.collect(Collectors.toList());

		return serviceDTOS;



    }

	public AssignmentOutputModel assignServicesToAppointment(AssignmentInputModel assignmentInputModel) {

		Integer appointmentId = assignmentInputModel.getAppointmentId();
		List<Integer> inputIds = assignmentInputModel.getServiceIds();

		Appointment appointment = AppointmentRepository.findById(appointmentId).get();
		List<ClinicService> newServices = cLinicServiceRepository.findByIds(inputIds);
		appointment.setServices(newServices);
		AppointmentRepository.save(appointment);
		System.out.println("successfully update");

		String currentIdslistString = inputIds.toString();
		currentIdslistString = currentIdslistString.substring(1, currentIdslistString.length()-1);
		AssignmentOutputModel assignmentOutputModel = new AssignmentOutputModel();
		assignmentOutputModel.setServiceIds(currentIdslistString);
		return assignmentOutputModel;
	}

	public Integer calculateTotalPrice(List<CustomerService> customerServices) {
		Integer total = 0;
		for(CustomerService customerService : customerServices){
			total += customerService.getPrice();
		}
		return total;
	}

	public void approveAppointment(int appointmentId) {
		Appointment appointment = AppointmentRepository.findById(appointmentId).get();
		appointment.setStatus(AppointmentStatus.APPROVAL);
		AppointmentRepository.save(appointment);
	}

	public void cancelAppointment(int appointmentId) {
		Appointment appointment = AppointmentRepository.findById(appointmentId).get();
		appointment.setStatus(AppointmentStatus.CANCELLED);
		AppointmentRepository.save(appointment);
	}

	public void setInvoicedStatus(int appointmentId) {
		Appointment appointment = AppointmentRepository.findById(appointmentId).get();
		appointment.setStatus(AppointmentStatus.INVOICED);
		AppointmentRepository.save(appointment);
	}

	public void setFinishedStatus(int appointmentId) {
		Appointment appointment = AppointmentRepository.findById(appointmentId).get();
		appointment.setStatus(AppointmentStatus.FINISHED);
		AppointmentRepository.save(appointment);
	}

    public List<ClinicService> findBySpecialization(Specialization specialization) {
		return cLinicServiceRepository.findBySpecialization(specialization);
    }

	public void savePriceForAppointment(Appointment appointment) {
		AppointmentRepository.save(appointment);
	}
}