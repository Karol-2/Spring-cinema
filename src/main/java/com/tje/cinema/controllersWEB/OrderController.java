package com.tje.cinema.controllersWEB;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.OrderService;
import com.tje.cinema.services.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final ScreeningService screeningService;


    @Autowired
    public OrderController(OrderService orderService, ScreeningService screeningService) {
        this.orderService = orderService;
        this.screeningService = screeningService;
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
            Screening screening = screeningService.getscreeningById(reservation.getScreening().getScreeningId());
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



}
