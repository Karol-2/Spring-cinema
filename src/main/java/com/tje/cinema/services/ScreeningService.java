package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.repositories.MovieRepository;
import com.tje.cinema.repositories.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningService {
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    private final ScreeningRepository screeningRepository;
    @Autowired
    public ScreeningService(MovieService movieService, ScreeningRepository screeningRepository, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void initialize() {
        setInitialScreenings();
    }

    public Screening addscreening(Screening screening){
        System.out.println("Dodanie senasu dla "+ screening.getMovieId() + ", " + screening.getDateAndTime());
        return this.screeningRepository.save(screening);
    }

    public List<Screening> getAllscreening(){
        return this.screeningRepository.findAll();
    }
    public void removeById(long id){
        this.screeningRepository.deleteById(id);
    }

    public Screening getscreeningById (long id) throws RuntimeException{
        return this.screeningRepository.findById(id).orElse(null);
    }

    public List<Screening> getscreeningesByDate(LocalDate date){
        List<Screening> allScreenings = this.screeningRepository.findAll();

        return allScreenings.stream()
                .filter(screening -> screening.getDateAndTime().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public Screening editscreening(long screeningId, Screening updatedScreening) {
        Screening existingScreening = getscreeningById(screeningId);

        existingScreening.setMovieId(updatedScreening.getMovieId());
        existingScreening.setDateAndTime(updatedScreening.getDateAndTime());
        existingScreening.setTakenSeats(updatedScreening.getTakenSeats());

        Movie movieObj = movieService.getMovieById(updatedScreening.getMovieId());
        existingScreening.setMovie(movieObj);

        System.out.println("Edycja screeningu o id " + screeningId);
        return this.screeningRepository.save(existingScreening);

    }

    public void removeAllscreeningOfMovie(long movieId){
        this.screeningRepository.deleteByMovieId(movieId);
    }


    public void setInitialScreenings() {
        List<Screening> existingScreenings = this.screeningRepository.findAll();
        if(existingScreenings.size() == 0){
            ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

            Map<String, Screening> screeningBeans = context.getBeansOfType(Screening.class);

            for (Screening scrBean : screeningBeans.values()) {
                Screening screening = scrBean;

                Optional<Movie> foundMovie = this.movieRepository.findById(screening.getMovieId());
                foundMovie.ifPresent(movie -> {
                    screening.setMovie(movie);
                    System.out.println("Adding new screening from beans");
                    this.screeningRepository.save(screening);
                });

            }
        }

    }





}
