package com.tje.cinema.controllersWEB;

import com.tje.cinema.domain.Order;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.domain.User;
import com.tje.cinema.repositories.ReservationRepository;
import com.tje.cinema.services.ScreeningService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class SeatController {

    private final ScreeningService screeningService;
    private final ReservationRepository reservationRepository;

    public SeatController(ScreeningService screeningService, ReservationRepository reservationRepository) {
        this.screeningService = screeningService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/seats/{screeningId}")
    public String seats(@PathVariable Long screeningId,
                        @RequestParam(name = "error", required = false) String error,
                        Model model, HttpSession session) throws ParseException {



        try {

            Screening screening = this.screeningService.getscreeningById(screeningId);

            List<Reservation> matchingReservations =this.reservationRepository.getReservationsByScreeningId(screening.getScreeningId());
            List<String> reservedSeats = this.makeOneSeatArray(matchingReservations);
            model.addAttribute("reservedSeats", reservedSeats);

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

    private List<String> makeOneSeatArray (List<Reservation> reservationList){
        return reservationList.stream()
                .flatMap(reservation -> reservation.getReservedSeats().stream())
                .collect(Collectors.toList());
    }


    @PostMapping("/seats")
    public String handleSeatSelection(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                      @RequestParam(name = "screeningId") Long screeningId,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (selectedSeats != null) {
            // create reservation
            Screening screening = screeningService.getscreeningById(screeningId);
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
            Screening screening = this.screeningService.getscreeningById(screeningIdLong);

            List<Reservation> matchingReservations =this.reservationRepository.getReservationsByScreeningId(screening.getScreeningId());
            List<String> reservedSeats = this.makeOneSeatArray(matchingReservations);

            model.addAttribute("reservedSeats", reservedSeats);
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
