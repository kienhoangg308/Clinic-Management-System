package com.clinic.admin.invoice;

import com.clinic.admin.appointment.AppointmentRepository;
import com.clinic.admin.record.RecordRepository;
import com.clinic.admin.util.PdfGeneratorUtil;
import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.Invoice;
import com.clinic.common.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;


@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RecordRepository recordRepository;


    private final PdfGeneratorUtil pdfGeneratorUtil;

    public InvoiceService(PdfGeneratorUtil pdfGeneratorUtil) {
        this.pdfGeneratorUtil = pdfGeneratorUtil;
    }


    public void save(Invoice invoice) {
        invoiceRepository.save(invoice);
    }


//    public File generatePdfForInvoice(int invoiceId) {
//        Invoice invoice = invoiceRepository.findById(invoiceId).get();
//
//        return pdfGeneratorUtil.generatePdfFromInvoice(invoice);
//    }

    public File generatePdfForInvoice(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();

        return pdfGeneratorUtil.generatePdfFromInvoice(appointment);
    }

    public File generatePdfForRecord(int recordId) {
        Record record = recordRepository.findById(recordId).get();
        return pdfGeneratorUtil.generatePdfFromRecord(record);
    }
}
