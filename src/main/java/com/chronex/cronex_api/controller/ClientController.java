package com.chronex.cronex_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/client")
public class ClientController {

    @GetMapping()    
    public String getTeste() {
        return "Client information";
    }

}
