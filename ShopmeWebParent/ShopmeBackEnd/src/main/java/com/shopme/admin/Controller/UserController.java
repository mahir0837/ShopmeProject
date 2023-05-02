package com.shopme.admin.Controller;

import com.shopme.admin.Exception.UserNotFountException;
import com.shopme.admin.Service.UserService;
import com.shopme.admin.Service.impl.UserServiceImpl;
import com.shopme.admin.Util.FileUploadUtil;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }
    @GetMapping("/users")
    public String listFirstPage(Model model, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return listByPage(1,model);
    }
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum")int pageNum , Model model){
        Page<User>page=userService.listByPage(pageNum);
        List<User>listUsers=page.getContent();
        long startCount= (long) (pageNum - 1) * UserServiceImpl.USERS_PER_PAGE+1;
        long endCount=startCount+UserServiceImpl.USERS_PER_PAGE-1;
        if (endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers",listUsers);
        return "users";
    }
    @GetMapping("/users/new")
    public String newUSer(Model model){
        model.addAttribute("user",new User());
        model.addAttribute("listRoles",userService.listRoles());
        model.addAttribute("pageTitle","Create New User");
       return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image")MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());


            user.setPhotos(fileName);

            User savedUser=userService.save(user);


//            File path=new File("C:\\Users\\Owner\\IdeaProjects\\ShopmeProject\\ShopmeWebParent\\ShopmeBackEnd");
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        }else{
            if (user.getPhotos().isEmpty())user.setPhotos(null);
            userService.save(user);
        }
//

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String editUSer(@PathVariable(name="id") Integer id, RedirectAttributes redirectAttributes,Model model){
       try {
           User user=userService.getUserById(id);
           model.addAttribute("user",user);
           model.addAttribute("pageTitle","Edit User (ID : "+ id+ ")");
           model.addAttribute("listRoles",userService.listRoles());

           return "user_form";
       } catch (UserNotFountException e) {
           redirectAttributes.addFlashAttribute("message",e.getMessage());
           return "redirect:/users";
       }

    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes){
        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","The user Id "+ id+" has been deleted successfully.");
        } catch (UserNotFountException e) {
            redirectAttributes.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/users";
    }
    @GetMapping("users/{id}/enabled/{status}")
    public String updateUserEnableUser(@PathVariable("id") Integer id,@PathVariable("status") Boolean enabled,
    RedirectAttributes redirectAttributes){

        userService.updateUserEnabledStatus(id, enabled);
        String status=enabled?"enabled":"disabled";
        String message="The user Id: "+ id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }
}
