package com.tje.cinema.controllers;

import com.tje.cinema.domain.Seans;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RepertuarController {
    @Autowired
    private RepertuarService repertuarService ;

    @PostMapping("/movies")
    public String showMovies(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Seans> seances = repertuarService.getSeansesByDate(date);

        model.addAttribute("seances", seances);
        model.addAttribute("selectedDate", date);
        return "moviesPage";
    }
}
