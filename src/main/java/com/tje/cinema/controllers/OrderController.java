package com.tje.cinema.controllers;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.OrderService;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final RepertuarService repertuarService;
    private final EntityManager entityManager;

    @Autowired
    public OrderController(OrderService orderService, RepertuarService repertuarService, EntityManager entityManager) {
        this.orderService = orderService;
        this.repertuarService = repertuarService;
        this.entityManager = entityManager;
    }

    @GetMapping("/orders")
    public String orders(Model model,  HttpSession session) throws ParseException {
        User user = (User)session.getAttribute("user");
        List<Order> orders= orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders",orders);
        System.out.println(orders);
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
        System.out.println(reservations);

        for (int index = 0; index < reservations.size(); index++) {
            Reservation reservation = reservations.get(index);
            Screening screening = repertuarService.getscreeningById(reservation.getScreening().getScreeningId());
            System.out.println(screening.getMovie().getTitle());
            if (new HashSet<>(screening.getTakenSeatsWithoutId()).containsAll(reservation.getReservedSeats())) {
                String errorMessage = "Someone has already reserved your seats for " + screening.getMovieTitle() +
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

    @GetMapping("/seats/{screeningId}")
    public String seats(@PathVariable Long screeningId,
                        @RequestParam(name = "error", required = false) String error,
                        Model model,HttpSession session) throws ParseException {
        try {
            Screening screening = this.repertuarService.getscreeningById(screeningId);

                    // screening is in order   seats/1/edit?previousSeats=%5BB-3%5D
            if (session.getAttribute("order") != null) {
                Order existingOrder = (Order) session.getAttribute("order");

                List<Screening> orderScreenings = existingOrder.getReservations().stream()
                        .map(reservation -> reservation.getScreening())
                        .collect(Collectors.toList());

                // if there is already a screening in the cart redirect to edit
                for (Screening orderScreening : orderScreenings) {
                    // Check if the screening matches any in the cart
                    if (orderScreening.getScreeningId().equals(screening.getScreeningId())) {
                        Reservation reservation = existingOrder.getReservations().stream()
                                .filter(res -> Objects.equals(res.getScreening().getScreeningId(), screeningId))
                                .findFirst().orElse(null);

                        assert reservation != null;
                        System.out.println(reservation.getReservedSeats());

                        // Encode
                        String encodedReservedSeats;
                        try {
                            encodedReservedSeats = URLEncoder.encode(reservation.getReservedSeats().toString(), StandardCharsets.UTF_8.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            return "redirect:/error";
                        }

                        return "redirect:/seats/" + screeningId + "/edit?previousSeats=" + encodedReservedSeats;
                    }
                }
            }

            //  screening is not in order
            model.addAttribute("error",error);
            model.addAttribute("screening", screening);
            model.addAttribute("screeningId", screening.getScreeningId());
            System.out.println(screening);
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatPage";
    }


    @PostMapping("/seats")
    public String handleSeatSelection(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                      @RequestParam(name = "screeningId") Long screeningId,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (selectedSeats != null) {
            // create reservation
            Screening screening = repertuarService.getscreeningById(screeningId);
            Reservation reservation = new Reservation(screening,selectedSeats,(User)session.getAttribute("user"));

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
        return "redirect:/seats/"+screeningId;
    }

    @GetMapping("/seats/{screeningId}/edit")
    public String seatsEdit(@PathVariable String screeningId,
                            @RequestParam("previousSeats") String previousSeatsParam,
                            @RequestParam(name = "error", required = false) String error ,
                            Model model) throws ParseException {
        long screeningIdLong = Long.parseLong(screeningId);
        String[] previousSeats = previousSeatsParam
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(", ");
        System.out.println("screeningId: " + screeningIdLong);
        System.out.println("prev seats: " + Arrays.toString(previousSeats));

        try {
            Screening screening = this.repertuarService.getscreeningById(screeningIdLong);
            model.addAttribute("previousSeats", previousSeats);
            model.addAttribute("screening", screening);
            model.addAttribute("error",error);
            System.out.println(screening);
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatEditPage";
    }

    @PostMapping("/editReservation")
    public String editReservation(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                  @RequestParam(name = "screeningId") Long screeningId,
                                  HttpSession session,
                                  HttpServletRequest request,
                                              RedirectAttributes redirectAttributes,
                                  Model model) throws ParseException {

        if (selectedSeats != null) {
            // find reservation
            Order userOrder = (Order)session.getAttribute("order");
            userOrder.getReservations().stream()
                    .filter((res) -> res.getScreening().getScreeningId().equals(screeningId))
                    .findFirst()
                    .ifPresent((res) -> res.setReservedSeats(selectedSeats));
            userOrder.setPrice(userOrder.calculateCost(userOrder.getReservations()));

            session.setAttribute("order", userOrder);

            return "redirect:/cart";

        }
        redirectAttributes.addAttribute("error","Choose at least one seat!");
        String referer = "redirect:" + request.getHeader("Referer");
        return referer;
    }

    @PostMapping("/deleteReservation")
    public String deleteReservation(@RequestParam(name = "screeningId") Long screeningId,
                                    HttpSession session) throws ParseException {

        Order userOrder = (Order) session.getAttribute("order");
        List<Reservation> updatedReservations = userOrder.getReservations().stream()
                .filter(res -> !res.getScreening().getScreeningId().equals(screeningId))
                .collect(Collectors.toList());

        userOrder.setReservations(updatedReservations);

        session.setAttribute("order", userOrder);

        return "redirect:/cart";
    }

}
