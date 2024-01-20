package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.setInitialMovies();
    }

    public Movie addMovie(Movie movie){
        System.out.println("Add movie: "+movie.getTitle());
        return movieRepository.save(movie);
    }
    public void removeMovieById(long id) {
        movieRepository.deleteById(id);
}
    public List<Movie> getAllMovies(){
        return (List<Movie>) movieRepository.findAll();
    }
    public Movie getMovieById(long id) throws RuntimeException {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie getMovieByTitle(String title){
        return movieRepository.findByTitle(title).orElse(null);
    }

    public Movie editMovie(long id, Movie updatedMovie) throws RuntimeException {
        Movie existingMovie = movieRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        if (existingMovie != null) {
            existingMovie.setTitle(updatedMovie.getTitle());
            existingMovie.setGenre(updatedMovie.getGenre());
            existingMovie.setYear(updatedMovie.getYear());
            existingMovie.setDirector(updatedMovie.getDirector());
            existingMovie.setActors(updatedMovie.getActors());
            existingMovie.setTrailerLink(updatedMovie.getTrailerLink());
            existingMovie.setPhotos(updatedMovie.getPhotos());

            System.out.println("Edit movie: " + existingMovie.getTitle());
            return movieRepository.save(existingMovie);
        } else {
            throw new RuntimeException("Movie not found with id: " + id);
        }
    }

    public void setInitialMovies() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Map<String, Movie> movieBeans = context.getBeansOfType(Movie.class);

        for (Movie movieBean : movieBeans.values()) {
            Movie movie = (Movie) movieBean;
            System.out.println("Bean, " + movie.getTitle());

            Optional<Movie> existingMovie = this.movieRepository.findByTitle(movieBean.getTitle());

            if (!existingMovie.isPresent()) {

                this.movieRepository.save(movieBean);
                System.out.println("Add movie from beans: "+movieBean.getTitle());
            }
        }
    }

}
