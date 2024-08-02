package com.example.ShopShoes.controller.Auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error-404")
@AllArgsConstructor
public class ErrorController {

    @GetMapping
    public String error404(){
        return "fe/404";
    }

}
