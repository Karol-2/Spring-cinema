package com.tje.cinema.controllersREST;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.ScreeningService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/screenings")
public class RestScreeningController {

    private final ScreeningService screeningService;
    private final MovieService movieService;

    public RestScreeningController(ScreeningService screeningService, MovieService movieService) {
        this.screeningService = screeningService;
        this.movieService = movieService;
    }

    @GetMapping()
    public ResponseEntity<List<Screening>> getAllScreenings(){
        return ResponseEntity.ok(screeningService.getAllscreening());
    }

    @GetMapping("/date")
    public ResponseEntity<List<Screening>> getAllScreeningsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok(screeningService.getscreeningesByDate(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getScreeningById(@PathVariable Long id){
        Screening scr = screeningService.getscreeningById(id);

        if(scr != null){
            return new ResponseEntity<>(scr, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Screening not found with id: " + id,HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping()
    public ResponseEntity<?> addScreening(@RequestBody @Valid Screening screening){
        Screening result = this.screeningService.addscreening(screening);

        try{
            Movie foundMovie = movieService.getMovieById(screening.getMovieId());
            if( foundMovie == null){
                return new ResponseEntity<>("Movie with this id doesn't exist: " + screening.getMovieId(),HttpStatus.NOT_FOUND);
            } else {
                result.setMovie(foundMovie);
            }
        } catch (RuntimeException ignored){}
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editScreening(@RequestBody @Valid Screening screening, @PathVariable Long id){
        try{
            Screening editedScreening = this.screeningService.editscreening(id,screening);

            Movie foundMovie = movieService.getMovieById(screening.getMovieId());
            if( foundMovie == null){
                return new ResponseEntity<>("Movie with this id doesn't exist: " + screening.getMovieId(),HttpStatus.BAD_REQUEST);
            } else {
                editedScreening.setMovie(foundMovie);
            }

            return new ResponseEntity<>(editedScreening,HttpStatus.OK) ;
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScreeningById(@PathVariable Long id) {
        try{
            Screening screening =  screeningService.getscreeningById(id);
            if(screening == null){
                return new ResponseEntity<>("Screening doesn't exist!",HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException ignored) {}
        screeningService.removeById(id);
        return ResponseEntity.ok("Deleted screening with id: " + id);

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
