package com.clinic.admin.record;

import com.clinic.admin.appointment.AppointmentNotFoundException;
import com.clinic.admin.appointment.AppointmentService;
import com.clinic.admin.invoice.InvoiceService;
import com.clinic.admin.service.ClinicServiceService;
import com.clinic.common.entity.*;
import com.clinic.common.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Controller
public class RecordController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClinicServiceService clinicServiceService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private RecordRepository recordRepository;


    @GetMapping("/records/new/appointment/{appointmentId}")
    public String newRecord(@PathVariable(name = "appointmentId") Integer appointmentId, Model model) throws AppointmentNotFoundException {
        Appointment appointment = appointmentService.get(appointmentId);
        List<ClinicService> listServices = clinicServiceService.listAll();

        model.addAttribute("listServices", listServices);
        model.addAttribute("customer", appointment.getCustomer());
        model.addAttribute("appointment", appointment);
        model.addAttribute("record", new Record());
        model.addAttribute("pageTitle", "New Record");
        return "/record/record_form1";
    }

@PostMapping("/records/save")
public String saveRecord(Record record){
    recordService.save(record);
    System.out.println(record.getAppointment().getDoctor().getFirstName());
    return getRedirectURLtoAffectedRecord(record);
}

    private String getRedirectURLtoAffectedRecord(Record record) {
        return "redirect:/records/page/1?sortField=id&sortDir=asc&keyword=" + record.getId();
    }

    @GetMapping("/records")
    public String listFirstPage(Model model) {
        return listByPage(1,model, "id","asc", null);
    }

    @GetMapping("/records/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {
//			if (sortDir ==  null || sortDir.isEmpty()) {
//				sortDir = "asc";
//			}



        Page<Record> page = recordService.listByPage( pageNum, sortDir,sortField, keyword);
        List<Record> listRecords = page.getContent();


        System.out.println("I want to check if this get wrong");
//        System.out.println("Sort Order: " + sortDir);
//        System.out.println("Page Num: " + pageNum);

        long startCount = (pageNum - 1) * recordService.RECORDS_PER_PAGE + 1;
        long endCount = startCount + recordService.RECORDS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);

        model.addAttribute("listRecords", listRecords);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "record/records";
    }


    @GetMapping("/records/edit/{id}")
    public String editRecord(@PathVariable(name = "id") Integer id, Model model,
                                  RedirectAttributes ra) {
        try {
            Record record = recordService.get(id);
            Appointment appointment = record.getAppointment();


			model.addAttribute("record", record);
            model.addAttribute("appointment", appointment);
            model.addAttribute("pageTitle", "Edit Appointment (ID: " + id + ")");

            return "record/record_detail";
        } catch (RecordNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/records";
        }
    }


    @GetMapping("/records/download/{recordId}")
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable("recordId") int recordId) {
        try {
            File invoicePdf = invoiceService.generatePdfForRecord(recordId);
            Record record = recordRepository.findById(recordId).get();
            Integer appointmentId = record.getAppointment().getId();
            appointmentService.setInvoicedStatus(appointmentId);
            HttpHeaders respHeaders = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType("application/pdf");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentLength(invoicePdf.length());
            respHeaders.setContentDispositionFormData("attachment", invoicePdf.getName());
            InputStreamResource isr = new InputStreamResource(new FileInputStream(invoicePdf));
            return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
        } catch (FileNotFoundException e) {
//            log.error("Error while generating pdf for download, error: {} ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
