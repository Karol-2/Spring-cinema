package com.tje.cinema.controllers;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.OrderService;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final RepertuarService repertuarService;

    @Autowired
    public OrderController(OrderService orderService, RepertuarService repertuarService) {
        this.orderService = orderService;
        this.repertuarService = repertuarService;
    }

    @GetMapping("/orders")
    public String orders(Model model,  HttpSession session) throws ParseException {
        User user = (User)session.getAttribute("user");
        List<Order> orders= orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders",orders);
        return "userOrdersPage";
    }
    @GetMapping("/cart")
    public String cart(Model model,
                       @RequestParam(name = "error", required = false) String error) throws ParseException {
        model.addAttribute("error",error);
        return "cartPage";
    }

    @PostMapping("/order-validate")
    public String validateOrder(HttpSession session, RedirectAttributes redirectAttributes){
        Order order = (Order)session.getAttribute("order");
        //check if seats are still free
        List<Reservation> reservations = order.getReservations();

        for (int index = 0; index < reservations.size(); index++) {
            Reservation reservation = reservations.get(index);
            Seans seans = repertuarService.getSeansById(reservation.getSeansId());
            if (new HashSet<>(seans.getTakenSeatsWithoutId()).containsAll(reservation.getReservedSeats())) {
                String errorMessage = "Someone has already reserved your seats for " + seans.getMovieTitle() +
                         ", those tickets will be removed from cart";

                reservations.remove(index);  // remove from order
                order.setReservations(reservations);

                // inform user
                redirectAttributes.addAttribute("error", errorMessage);
                return "redirect:/cart";
            }
        }

        //success
        orderService.addOrder(order);
        session.removeAttribute("order");

        return "redirect:/payment?orderId="+order.getOrderId();
    }
    @PostMapping("/clearCart")
    public String clearCart(@SessionAttribute(name = "order", required = false) Order order,
                            HttpSession session) {
        session.removeAttribute("order");
        return "redirect:/cart";
    }

    @GetMapping("/seats/{seansId}")
    public String seats(@PathVariable Long seansId,
                        @RequestParam(name = "error", required = false) String error,
                        Model model) throws ParseException {
        try {
            Seans seans = this.repertuarService.getSeansById(seansId);
            model.addAttribute("error",error);
            model.addAttribute("seans", seans);
            model.addAttribute("seansId", seans.getSeansId());
            System.out.println(seans);
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatPage";
    }


    @PostMapping("/seats")
    public String handleSeatSelection(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                      @RequestParam(name = "seansId") Long seansId,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

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
                Order order = new Order(resArr, LocalDateTime.now(),(User)session.getAttribute("user"));
                session.setAttribute("order", order);
            }
            return "redirect:/cart";

        }

        redirectAttributes.addAttribute("error","Choose at least one seat!");
        return "redirect:/seats/"+seansId;
    }

    @GetMapping("/seats/{seansId}/edit")
    public String seatsEdit(@PathVariable String seansId,
                            @RequestParam("previousSeats") String previousSeatsParam,
                            Model model) throws ParseException {
        long seansIdLong = Long.parseLong(seansId);
        String[] previousSeats = previousSeatsParam
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(", ");
        System.out.println("seansId: " + seansIdLong);
        System.out.println("prev seats: " + Arrays.toString(previousSeats));

        try {
            Seans seans = this.repertuarService.getSeansById(seansIdLong);
            model.addAttribute("previousSeats", previousSeats);
            model.addAttribute("seans", seans);
            System.out.println(seans);
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatEditPage";
    }

    @PostMapping("/editReservation")
    public String editReservation(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                  @RequestParam(name = "seansId") Long seansId,
                                  HttpSession session,
                                  Model model) throws ParseException {

        if (selectedSeats != null) {
            // find reservation
            Order userOrder = (Order)session.getAttribute("order");
            userOrder.getReservations().stream()
                    .filter((res) -> res.getSeans().getSeansId().equals(seansId))
                    .findFirst()
                    .ifPresent((res) -> res.setReservedSeats(selectedSeats));
            userOrder.setPrice(userOrder.calculateCost(userOrder.getReservations()));

            session.setAttribute("order", userOrder);

            return "redirect:/cart";

        }
        model.addAttribute("error","Choose at least one seat!"); //TODO: fix redirecting after empty choice

        return "redirect:/cart";
    }

    @PostMapping("/deleteReservation")
    public String deleteReservation(@RequestParam(name = "seansId") Long seansId,
                                    HttpSession session) throws ParseException {

        Order userOrder = (Order) session.getAttribute("order");
        List<Reservation> updatedReservations = userOrder.getReservations().stream()
                .filter(res -> !res.getSeans().getSeansId().equals(seansId))
                .collect(Collectors.toList());

        userOrder.setReservations(updatedReservations);

        session.setAttribute("order", userOrder);

        return "redirect:/cart";
    }

}
