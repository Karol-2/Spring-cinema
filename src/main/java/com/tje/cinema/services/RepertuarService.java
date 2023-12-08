package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Seans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepertuarService {
    @Autowired
    private MovieService movieService;
    @PostConstruct
    public void initialize() {
        setInitialSeanses();
    }

    private final List<Seans> seansDatabase = new ArrayList<Seans>();
    private long seansIdCounter = 1;


    public void addSeans(Seans seans){
        seans.setSeansId(seansIdCounter++);
        System.out.println("Dodanie senasu, o id" + seans.getSeansId() + " dla "+ seans.getMovieId() + ", " + seans.getDateAndTime());
        this.seansDatabase.add(seans);
    }

    public void addSeans(Seans seans, long id){
        Movie movieObj = movieService.getMovieById(seans.getMovieId());
        seans.setMovie(movieObj);
        seans.setSeansId(id);

        System.out.println("Dodanie senasu, o id" + seans.getSeansId() + " dla "+ seans.getMovieId() + ", " + seans.getDateAndTime());
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
                    return new Seans(seans.getSeansId(),movie,movie.getId(),seans.getDateAndTime());
                })
                .collect(Collectors.toList());
    }

    public void editSeans(long seansId, Seans updatedSeans) {
        Seans existingSeans = getSeansById(seansId);

        existingSeans.setMovieId(updatedSeans.getMovieId());
        existingSeans.setDateAndTime(updatedSeans.getDateAndTime());
        existingSeans.setTakenSeats(updatedSeans.getTakenSeats());

        Movie movieObj = movieService.getMovieById(updatedSeans.getMovieId());
        existingSeans.setMovie(movieObj);

        System.out.println("Edycja seansu o id " + seansId);

    }



    public void setInitialSeanses() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        // Dodanie Seansów z beans
        Map<String, Seans> seansBeans = context.getBeansOfType(Seans.class);
        seansBeans.values().forEach(seans -> this.addSeans(seans,seansIdCounter++));
    }


}
