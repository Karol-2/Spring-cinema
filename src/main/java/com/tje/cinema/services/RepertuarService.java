package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Seans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepertuarService {
    @Autowired
    private MovieService movieService;
    public RepertuarService(){
        this.setInitialSeanses();
    }

    private final List<Seans> seansDatabase = new ArrayList<Seans>();
    private long seansIdCounter = 1;


    public void addSeans(Seans seans){
        System.out.println("Dodanie senasu, o id" + seans.getSeansId() + " dla "+ seans.getMovieId() + ", " + seans.getDateAndTime());
        seans.setSeansId(seansIdCounter++);
        this.seansDatabase.add(seans);
    }

    public Seans getSeansById (long id){
        return seansDatabase.stream()
                .filter(seans -> seans.getSeansId().equals(id))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Seans not found with id: " + id));
    }

    public List<Seans> getSeansesByDate(LocalDate date){
        return seansDatabase.stream()
                .filter(seans -> seans.getDateAndTime().toLocalDate().isEqual(date))
                .map(seans -> {
                    Movie movie = movieService.getMovieById(seans.getMovieId());
                    return new Seans(movie,seans.getDateAndTime());
                })
                .collect(Collectors.toList());
    }

    public void setInitialSeanses() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        // Dodanie Seansów z beans
        Map<String, Seans> seansBeans = context.getBeansOfType(Seans.class);
        seansBeans.values().forEach(seans -> this.addSeans(seans));
    }
}
