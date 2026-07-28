package com.chronex.cronex_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/client")
public class ClientController {

    @GetMapping 
    public void getClient(@RequestBody String entity) {
    }

    @PostMapping
    public void createClient(@RequestBody String entity) {

    }

    @PutMapping("{id}")
    public void updateClient(@PathVariable String id, @RequestBody String entity) {

    }

    @DeleteMapping("{id}")
    public void deleteClient(@PathVariable String id) {

    }
}
