package com.clinic.admin.invoice;


import com.clinic.admin.appointment.AppointmentService;
import com.clinic.admin.service.ClinicServiceService;
import com.clinic.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
import java.io.IOException;
import java.util.List;

@Controller
public class InvoiceController {

    @Autowired
    private ClinicServiceService clinicServiceService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/invoices/new")
    public String newServices(Model model) {
        List<ClinicService> listServices = clinicServiceService.listAll();

        model.addAttribute("invoice", new Invoice());
        model.addAttribute("listServices", listServices);
        model.addAttribute("pageTitle", "Create New Invoice");

        return "report_form";
    }


    @PostMapping("/invoices/save")
    public String saveInvoice(Invoice invoice,
                                  RedirectAttributes ra) throws IOException {
//
        invoiceService.save(invoice);

        ra.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/appointments";
    }



    @GetMapping("/invoices/appointment/download/{appointmentId}")
    public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable("appointmentId") int appointmentId) {
        try {
            File invoicePdf = invoiceService.generatePdfForInvoice(appointmentId);
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
