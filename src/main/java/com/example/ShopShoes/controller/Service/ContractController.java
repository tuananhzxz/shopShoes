package com.example.ShopShoes.controller.Service;

import com.example.ShopShoes.dto.ContactDTO;
import com.example.ShopShoes.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/contact")
public class ContractController {

    private final ContactService contactService;

    @GetMapping
    public String contact(Model model){
        model.addAttribute("contact", new ContactDTO());
        return "fe/contact";
    }

    @PostMapping
    public String addContact(@Valid @ModelAttribute("contact") ContactDTO contactDTO, Model model, BindingResult result){
        if (result.hasErrors()) {
            return "fe/contact";
        }
        try {
            contactService.createContact(contactDTO);
            model.addAttribute("contact", new ContactDTO());
            model.addAttribute("successMessage", "Gửi liên hệ thành công.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Gửi liên hệ thất bại.");
        }
        return "fe/contact";
    }

}
