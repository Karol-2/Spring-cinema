package com.tje.cinema;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;

@Controller
public class AppController {

    @GetMapping("/")
    public String home(Model model) throws ParseException {
        return "home";
    }
}
