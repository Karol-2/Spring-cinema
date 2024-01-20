package com.tje.cinema.controllersWEB;

import com.tje.cinema.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Random;

@Controller
@Transactional
public class PaymentController {

    private final OrderService orderService;

    @Autowired
    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
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
            orderService.finalizeOrder(orderId);
            session.removeAttribute("order");
            return "redirect:/orders";
        }
        redirectAttributes.addAttribute("orderId",orderId);
        redirectAttributes.addAttribute("Codes don't match");

        return "redirect:/payment";
    }
}
