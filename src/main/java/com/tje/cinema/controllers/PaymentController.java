package com.tje.cinema.controllers;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.services.OrderService;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
public class PaymentController {

    private final OrderService orderService;
    private final RepertuarService repertuarService;

    @Autowired
    public PaymentController(OrderService orderService, RepertuarService repertuarService) {
        this.orderService = orderService;
        this.repertuarService = repertuarService;
    }

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
                                  HttpSession session,
                                  Model model) throws ParseException {
        if (code.equals(response)){
            //sukces
            Order order = orderService.getOrder(orderId);
            orderService.finalizeOrder(orderId);
            session.removeAttribute("order");

            // zarezerwowanie miejsc
            List<Reservation> reservationsList = order.getReservations();
            for (Reservation reservation : reservationsList) {

                Screening screening = reservation.getScreening();
                HashMap<Long, List<String>> existingSeats = screening.getTakenSeats();
                existingSeats.put(orderId,reservation.getReservedSeats());
                this.repertuarService.editscreening(screening.getScreeningId(), screening);
            }

            return "redirect:/orders";
        }
        redirectAttributes.addAttribute("orderId",orderId);
        redirectAttributes.addAttribute("Codes don't match");

        return "redirect:/payment";
    }
}
