/*
 * Copyright (C) 2023 Noom, Inc.
 */
package com.noom.interview.fullstack.sleep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello world!";
    }
}
