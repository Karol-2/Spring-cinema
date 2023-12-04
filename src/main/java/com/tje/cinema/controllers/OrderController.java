package com.tje.cinema.controllers;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private RepertuarService repertuarService;

    @PostMapping("/seats")
    public String handleSeatSelection(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                      @RequestParam(name = "seansId", required = true) Long seansId,
                                      HttpSession session,
                                      Model model) {
        System.out.println(seansId);
        if (selectedSeats != null) {
            // create reservation
            Seans seans = repertuarService.getSeansById(seansId);
            Reservation reservation = new Reservation(seans,selectedSeats,(User)session.getAttribute("user"));

            Object userOrder = session.getAttribute("order");
            if (userOrder != null){ // order already exists - add to it
               Order existingOrder = (Order)userOrder;
               existingOrder.addReservation(reservation);
               session.setAttribute("order", existingOrder);

            } else { // order doesn't exist - create new
                List<Reservation> resArr = new ArrayList<>();
                resArr.add(reservation);
                Order order = new Order(1,resArr, LocalDateTime.now(),(User)session.getAttribute("user"));
                session.setAttribute("order", order);
            }
            return "redirect:/cart";

        } else {
            System.out.println("No seats selected");
            model.addAttribute("error","Choose at least one seat!");
        }
        return "seatPage";
    }

    @GetMapping("/orders")
    public String orders(Model model) throws ParseException {

        return "userOrdersPage";
    }
    @GetMapping("/cart")
    public String cart(Model model) throws ParseException {

        return "cartPage";
    }
    @GetMapping("/order")
    public String order(Model model) throws ParseException {

        return "orderPage";
    }

    @GetMapping("/clearCart")
    public String clearCart(@SessionAttribute(name = "order", required = false) Order order, HttpSession session) {
        session.removeAttribute("order");
        return "redirect:/cart";
    }
}
