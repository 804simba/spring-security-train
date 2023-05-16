package com.simba.security.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/demo")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Demo")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello from demo");
    }
}
