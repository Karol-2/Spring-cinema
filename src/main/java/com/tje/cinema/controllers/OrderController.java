package com.tje.cinema.controllers;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String orders(Model model,  HttpSession session) throws ParseException {
        User user = (User)session.getAttribute("user");
        List<Order> orders= orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders",orders);
        return "userOrdersPage";
    }
    @GetMapping("/cart")
    public String cart(Model model) throws ParseException {
        return "cartPage";
    }

    @PostMapping("/clearCart")
    public String clearCart(@SessionAttribute(name = "order", required = false) Order order, HttpSession session) {
        session.removeAttribute("order");
        return "redirect:/cart";
    }
}
