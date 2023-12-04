package com.tje.cinema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.util.Random;

@Controller
public class PaymentController {
    @GetMapping("/payment")
    public String payment(@RequestParam("orderId") Long orderId,
                          @RequestParam(name = "error", required = false) String error,
                          Model model) throws ParseException {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        model.addAttribute("code",code);
        model.addAttribute("orderId",orderId);
        model.addAttribute("error",error);
        return "paymentPage";
    }

    @PostMapping("/payment")
    public String paymentChecking(@RequestParam("orderId") Long orderId,
                                  @RequestParam("code") String code,
                                  @RequestParam("response") String response,
                                  RedirectAttributes redirectAttributes,
                                  Model model) throws ParseException {
        if (code.equals(response)){
            //sukces, idź na myorders i zmień zamówienie na completed, usun z session order
            return "redirect:/orders";
        }
        redirectAttributes.addAttribute("orderId",orderId);
        redirectAttributes.addAttribute("Codes don't match");

        return "redirect:/payment";
    }
}
