package org.timolisa.springsecurityclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public String helloApi() {
        return "Hello, welcome to Simba's world";
    }
}
