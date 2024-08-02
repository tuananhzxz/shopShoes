package com.example.ShopShoes.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ShoesApiException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
}
