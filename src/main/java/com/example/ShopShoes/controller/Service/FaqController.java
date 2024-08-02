package com.example.ShopShoes.controller.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/faq")
public class FaqController {

    @GetMapping
    public String faq(){
        return "fe/faq";
    }

}
