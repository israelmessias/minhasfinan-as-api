package com.israelmessias.minhasfinancas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    @GetMapping("/")
    public String HelloWorld(){
        return "Salve pivete!!!";
    }
}
