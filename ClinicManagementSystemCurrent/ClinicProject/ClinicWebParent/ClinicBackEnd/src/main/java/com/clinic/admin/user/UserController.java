package com.clinic.admin.user;

import com.clinic.admin.FileUploadUtil;
import com.clinic.admin.service.ClinicServiceService;
import com.clinic.common.entity.Specialization;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ClinicServiceService serviceService;

//    @GetMapping("/users")
//    public String listFirstPage(Model model) {
//        return listByPage(1, model, "firstName", "asc", null);
//    }

//    @GetMapping("/{role}")
//    public String listFirstPage(@PathVariable(name = "role") String role,Model model) {
//        return listByPage(role,1, model, "firstName", "asc", null);
//    }

    @GetMapping("/staffs")
    public String listStaffFirstPage(Model model) {
        return listStaffByPage(1, model, "firstName", "asc", null);
    }


    @GetMapping("/doctors")
    public String listDoctorFirstPage(Model model) {
        return listDoctorByPage(1, model, "firstName", "asc", null);
    }




//    @GetMapping("/users/page/{pageNum}")
//    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
//                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,@Param("keyword") String keyword
//    ) {
//        System.out.println("Sort Field: " + sortField);
//        System.out.println("Sort Order: " + sortDir);
//
//        Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
//        List<User> listUsers = page.getContent();
//
//        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
//        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
//        if (endCount > page.getTotalElements()) {
//            endCount = page.getTotalElements();
//        }
//
//        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("startCount", startCount);
//        model.addAttribute("endCount", endCount);
//        model.addAttribute("totalItems", page.getTotalElements());
//        model.addAttribute("listUsers", listUsers);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("reverseSortDir", reverseSortDir);
//        model.addAttribute("keyword", keyword);
//
//        return "users";
//    }


    @GetMapping("/staffs/page/{pageNum}")
    public String listStaffByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,@Param("keyword") String keyword
    ) {
//        System.out.println("Sort Field: " + sortField);
//        System.out.println("Sort Order: " + sortDir);
        String roleName = "Staff";
        Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword, roleName);
        List<User> listUsers = page.getContent();
//        System.out.println("Key word: " + keyword);

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "staff/staffs";
    }


    @GetMapping("/doctors/page/{pageNum}")
    public String listDoctorByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                                  @Param("sortField") String sortField, @Param("sortDir") String sortDir,@Param("keyword") String keyword
    ) {
//        System.out.println("Sort Field: " + sortField);
//        System.out.println("Sort Order: " + sortDir);
        String roleName = "Doctor";
        Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword, roleName);
        List<User> listUsers = page.getContent();
        System.out.println("Key word: " + keyword);

        long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "doctor/doctors";
    }



    @GetMapping("/staffs/new")
    public String newStaff(Model model){
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New Staff");

        return "staff/staff_form";
    }

    @GetMapping("/doctors/new")
    public String newDoctor(Model model){
        User user = new User();
        List<Specialization> listSpecializations = serviceService.listSpecializations();
        model.addAttribute("listSpecializations", listSpecializations);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New Doctor");

        return "doctor/doctor_form";
    }


    @PostMapping("/staffs/save")
    public String saveStaff(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String roleName = "Staff";
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user, roleName);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user, roleName);
        }


        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

//        return getRedirectURLtoAffectedUser(user);
        return getRedirectURLtoAffectedStaff(user);
    }

    @PostMapping("/doctors/save")
    public String saveDoctor(User user, RedirectAttributes redirectAttributes,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String roleName = "Doctor";
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user, roleName);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);
            service.save(user, roleName);
        }


        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");

//        return getRedirectURLtoAffectedUser(user);
        return "redirect:/doctors";
    }




    private String getRedirectURLtoAffectedStaff(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/staffs/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }


    @GetMapping("/staffs/edit/{id}")
    public String editStaff(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit Staff (ID: " + id + ")");
            return "staff/staff_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/staffs";
        }
    }

    @GetMapping("/doctors/edit/{id}")
    public String editDoctor(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            User user = service.get(id);
            List<Specialization> listSpecializations = serviceService.listSpecializations();
            model.addAttribute("listSpecializations", listSpecializations);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit Doctor (ID: " + id + ")");
            return "doctor/doctor_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/doctors";
        }
    }



    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");


        }catch(UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/users";
    }


    @GetMapping("/staffs/delete/{id}")
    public String deleteStaff(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");


        }catch(UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/staffs";
    }

    @GetMapping("/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            service.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");


        }catch(UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/doctors";
    }



//    @GetMapping("/users/{id}/enabled/{status}")
//    public String updateUserStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes){
//        service.updateUserStatus(id, enabled);
//        String status = enabled ? "enabled" : "disabled";
//        String message = "The user ID " + id + " has been " + status;
//        redirectAttributes.addFlashAttribute("message", message);
//        return "redirect:/users";
//    }

    @GetMapping("/staffs/{id}/enabled/{status}")
    public String updateStaffStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes){
        service.updateUserStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/staffs";
    }


    @GetMapping("/doctors/{id}/enabled/{status}")
    public String updateDoctorStatus(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") boolean enabled, RedirectAttributes redirectAttributes){
        service.updateUserStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/doctors";
    }


    @GetMapping("/doctors/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = service.listAllDoctor();

        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/staffs/export/excel")
    public void exportStaffToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = service.listAllStaff();

        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }




}
