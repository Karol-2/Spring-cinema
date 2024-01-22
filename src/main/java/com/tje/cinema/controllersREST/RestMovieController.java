package com.tje.cinema.controllersREST;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class RestMovieController {

    private final MovieService movieService;

    public RestMovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping()
    public ResponseEntity<List<Movie>> getAllMovies(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id){
        Movie movie = movieService.getMovieById(id);

        if(movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else{
            return new ResponseEntity<>("Movie not found with id: " + id,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMovie(@RequestBody @Valid Movie movie){

        try{
            Movie foundMovie = movieService.getMovieByTitle(movie.getTitle());
            if (foundMovie != null){
                return new ResponseEntity<>("Movie with this title already exists!",HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ignored){}


        Movie result = this.movieService.addMovie(movie);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editMovie(@RequestBody @Valid Movie movieUpdated,@PathVariable Long id) {
        try{
            Movie editedMovie = this.movieService.editMovie(id,movieUpdated);
            return new ResponseEntity<>(editedMovie, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMovieById(@PathVariable Long id) {
        try{
            Movie movie =  movieService.getMovieById(id);
            if(movie == null){
                return new ResponseEntity<>("Movie doesn't exist!",HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException ignored) {}
        movieService.removeMovieById(id);
        return ResponseEntity.ok("Deleted Movie with id: " + id);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        HashMap<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldname = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldname, errorMsg);
        });
        return errors;
    }
}
