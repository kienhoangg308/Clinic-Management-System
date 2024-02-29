package com.clinic.admin.service;

import com.clinic.admin.FileUploadUtil;
import com.clinic.common.entity.ClinicService;
import com.clinic.common.entity.Specialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.ServiceNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
public class ClinicServiceController {

    @Autowired
    private ClinicServiceService serviceService;

    @GetMapping("/services")
    public String listFirstPage(Model model) {
        return listByPage(1,model, "name","asc", null);
    }

    @GetMapping("/services/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {
//			if (sortDir ==  null || sortDir.isEmpty()) {
//				sortDir = "asc";
//			}



        Page<ClinicService> page = serviceService.listByPage( pageNum, sortDir,sortField, keyword);
        List<ClinicService> listServices = page.getContent();


        System.out.println("Sort Field: " + sortField);
        System.out.println("Sort Order: " + sortDir);
        System.out.println("Page Num: " + pageNum);

        long startCount = (pageNum - 1) * serviceService.SERVICES_PER_PAGE + 1;
        long endCount = startCount + serviceService.SERVICES_PER_PAGE - 1;
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

        model.addAttribute("listServices", listServices);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "services/services";
    }


    @GetMapping("/services/new")
    public String newServices(Model model) {
        List<Specialization> listSpecializations = serviceService.listSpecializations();

        model.addAttribute("service", new ClinicService());
        model.addAttribute("listSpecializations", listSpecializations);
        model.addAttribute("pageTitle", "Create New Service");

        return "services/service_form";
    }

    @PostMapping("/services/save")
    public String saveService(ClinicService clinicService,
                              @RequestParam("fileImage") MultipartFile multipartFile,
                              RedirectAttributes ra) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            clinicService.setImage(fileName);

            ClinicService savedService = serviceService.save(clinicService);
            String uploadDir = "../service-images/" + savedService.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            serviceService.save(clinicService);
        }

        ra.addFlashAttribute("message", "The service has been saved successfully.");
        return "redirect:/services";
    }

    @GetMapping("/services/edit/{id}")
    public String editService(@PathVariable(name = "id") Integer id, Model model,
                               RedirectAttributes ra) {
        try {
            ClinicService clinicService = serviceService.get(id);
            List<Specialization> listSpecializations = serviceService.listSpecializations();
            model.addAttribute("listSpecializations", listSpecializations);

            model.addAttribute("service", clinicService);
            model.addAttribute("pageTitle", "Edit Service (ID: " + id + ")");

            return "services/service_form";
        } catch (ClinicServiceNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/services";
        }
    }

    @GetMapping("/services/{id}/enabled/{status}")
    public String updateServiceEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        serviceService.updateServiceEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The service ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/services";
    }

    @GetMapping("/services/delete/{id}")
    public String deleteService(@PathVariable(name = "id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            serviceService.delete(id);

            redirectAttributes.addFlashAttribute("message",
                    "The service ID " + id + " has been deleted successfully");
        } catch (ClinicServiceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/services";
    }
}
