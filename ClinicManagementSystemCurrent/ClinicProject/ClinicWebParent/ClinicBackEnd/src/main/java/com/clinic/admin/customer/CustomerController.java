package com.clinic.admin.customer;

import com.clinic.admin.FileUploadUtil;
import com.clinic.admin.user.UserNotFoundException;
import com.clinic.admin.user.UserService;
import com.clinic.common.entity.Customer;
import com.clinic.common.entity.User;
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

import java.io.IOException;
import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;


    @GetMapping("/customers")
    public String listCustomerFirstPage(Model model) {
        return listCustomerByPage(1, model, "firstName", "asc", null);
    }


    @GetMapping("/customers/page/{pageNum}")
    public String listCustomerByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                                     @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword
    ) {
//        System.out.println("Sort Field: " + sortField);
//        System.out.println("Sort Order: " + sortDir);
        //String roleName = "Customer";
        Page<Customer> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        List<Customer> listCustomers = page.getContent();

        long startCount = (pageNum - 1) * CustomerService.CUSTOMER_PER_PAGE + 1;
        long endCount = startCount + CustomerService.CUSTOMER_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "customer/customers";
    }


    @GetMapping("/customers/new")
    public String newCustomer(Model model){
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "Create New Customer");

        return "customer/customer_form";
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer user, RedirectAttributes redirectAttributes,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            Customer savedUser = service.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user);
        }


        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

//        return getRedirectURLtoAffectedUser(user);
        return "redirect:/customers";
    }


    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            Customer customer = service.get(id);
            model.addAttribute("customer", customer);
            model.addAttribute("pageTitle", "Edit Customer (ID: " + id + ")");
            return "customer/customer_form";
        } catch (CustomerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }


    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");


        }catch(CustomerNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/customers";
    }


    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes){
        service.updateUserStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/customers";
    }

}
