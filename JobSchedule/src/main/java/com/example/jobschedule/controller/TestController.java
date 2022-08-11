package com.example.jobschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MintChow
 * @create 2022-08-12-0:24
 */
@Controller
public class TestController {
    @RequestMapping("/")
    public String test(){
        return "forward:index.html";
    }
}
