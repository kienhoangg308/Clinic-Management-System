package com.clinic.admin.util;

import com.clinic.common.entity.Appointment;
import com.clinic.common.entity.Invoice;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.UUID;
//import org.dom4j.DocumentException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.context.IContext;
//
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//import org.thymeleaf.context.Context;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.UUID;

import com.clinic.common.entity.Record;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class PdfGeneratorUtil {

    private final SpringTemplateEngine templateEngine;
    private final String baseUrl;

    public PdfGeneratorUtil(SpringTemplateEngine templateEngine, @Value("${http://localhost:8080}") String baseUrl) {
        this.templateEngine = templateEngine;
        this.baseUrl = baseUrl;
    }



    public File generatePdfFromInvoice(Appointment appointment) {

        Context ctx = new Context();
        ctx.setVariable("appointment", appointment);
        String processedHtml = templateEngine.process("service_selection/invoice5", ctx);

        ITextRenderer renderer = new ITextRenderer();

        String fontPath = "C:\\Users\\Admin\\Downloads\\Noto_Sans\\static\\NotoSans-Regular.ttf";

        try {
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            System.out.println("Font added successfully");
            renderer.setDocumentFromString(processedHtml, baseUrl);
            renderer.layout();

            // ... the rest of your PDF generation code ...
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF", e);
        }


        String fileName = UUID.randomUUID().toString();
        FileOutputStream os = null;
        try {
            final File outputFile = File.createTempFile(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            renderer.createPDF(os, false);
            renderer.finishPDF();
            return outputFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public File generatePdfFromRecord(Record record) {

        Context ctx = new Context();
        ctx.setVariable("record", record);
        String processedHtml = templateEngine.process("record/record_pdf", ctx);

        ITextRenderer renderer = new ITextRenderer();

        String fontPath = "C:\\Users\\Admin\\Downloads\\Noto_Sans\\static\\NotoSans-Regular.ttf";

        try {
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            System.out.println("Font added successfully");
            renderer.setDocumentFromString(processedHtml, baseUrl);
            renderer.layout();

            // ... the rest of your PDF generation code ...
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF", e);
        }


        String fileName = UUID.randomUUID().toString();
        FileOutputStream os = null;
        try {
            final File outputFile = File.createTempFile(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            renderer.createPDF(os, false);
            renderer.finishPDF();
            return outputFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


//    public File generatePdfFromInvoice(Invoice invoice) {
//
//        Context ctx = new Context();
//        ctx.setVariable("invoice", invoice);
//        String processedHtml = templateEngine.process("invoice", ctx);
//
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(processedHtml, baseUrl);
//        renderer.layout();
//
//        String fileName = UUID.randomUUID().toString();
//        FileOutputStream os = null;
//        try {
//            final File outputFile = File.createTempFile(fileName, ".pdf");
//            os = new FileOutputStream(outputFile);
//            renderer.createPDF(os, false);
//            renderer.finishPDF();
//            return outputFile;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } finally {
//            if (os != null) {
//                try {
//                    os.close();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
}
