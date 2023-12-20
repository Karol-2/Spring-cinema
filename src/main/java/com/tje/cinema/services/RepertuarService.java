package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Screening;
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
    private final MovieService movieService;
    @Autowired
    public RepertuarService(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostConstruct
    public void initialize() {
        setInitialscreeninges();
    }

    private List<Screening> screeningDatabase = new ArrayList<Screening>();
    private long screeningIdCounter = 1;


    public void addscreening(Screening screening){
        screening.setScreeningId(screeningIdCounter++);
        System.out.println("Dodanie senasu, o id" + screening.getScreeningId() + " dla "+ screening.getMovieId() + ", " + screening.getDateAndTime());
        this.screeningDatabase.add(screening);
    }

    public void addscreening(Screening screening, long id){
        Movie movieObj = movieService.getMovieById(screening.getMovieId());
        screening.setMovie(movieObj);
        screening.setScreeningId(id);

        System.out.println("Dodanie senasu, o id" + screening.getScreeningId() + " dla "+ screening.getMovieId() + ", " + screening.getDateAndTime());
        this.screeningDatabase.add(screening);
    }
    public List<Screening> getAllscreening(){
        return this.screeningDatabase;
    }
    public void removeById(long id){
        screeningDatabase.removeIf(screening -> screening.getScreeningId() == id);
    }

    public Screening getscreeningById (long id) throws RuntimeException{
        return screeningDatabase.stream()
                .filter(screening -> screening.getScreeningId().equals(id))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Screening not found with id: " + id));
    }

    public List<Screening> getscreeningesByDate(LocalDate date){
        return screeningDatabase.stream()
                .filter(screening -> screening.getDateAndTime().toLocalDate().isEqual(date))
                .map(screening -> {
                    Movie movie = movieService.getMovieById(screening.getMovieId());
                    return new Screening(screening.getScreeningId(),movie,movie.getId(),screening.getDateAndTime());
                })
                .collect(Collectors.toList());
    }

    public void editscreening(long screeningId, Screening updatedScreening) {
        Screening existingScreening = getscreeningById(screeningId);

        existingScreening.setMovieId(updatedScreening.getMovieId());
        existingScreening.setDateAndTime(updatedScreening.getDateAndTime());
        existingScreening.setTakenSeats(updatedScreening.getTakenSeats());

        Movie movieObj = movieService.getMovieById(updatedScreening.getMovieId());
        existingScreening.setMovie(movieObj);

        System.out.println("Edycja screeningu o id " + screeningId);
    }

    public void removeAllscreeningOfMovie(long movieId){
        this.screeningDatabase = this.screeningDatabase.stream()
                .filter(screening -> screening.getMovieId() != movieId)
                .collect(Collectors.toList());
    }

    public void setInitialscreeninges() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        // Dodanie screeningów z beans
        Map<String, Screening> screeningBeans = context.getBeansOfType(Screening.class);
        screeningBeans.values().forEach(screening -> this.addscreening(screening,screeningIdCounter++));
    }


}
