package com.tje.cinema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {
    @PostMapping("/seats")
    public String handleSeatSelection(@RequestParam(name = "seat", required = false) String[] selectedSeats,
                                      @RequestParam(name = "seansId", required = true) Long seansId,
                                      Model model) {
        System.out.println(seansId);
        if (selectedSeats != null) {
            for (String seat : selectedSeats) {
                System.out.println("Selected seat: " + seat);
            }
        } else {
            System.out.println("No seats selected");
            model.addAttribute("error","Choose at least one seat!");
        }
        return "seatPage";
    }
}
