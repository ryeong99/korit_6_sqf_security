package com.study.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    public ResponseEntity<?> init() {
        return ResponseEntity.ok().body(null);
    }
}
