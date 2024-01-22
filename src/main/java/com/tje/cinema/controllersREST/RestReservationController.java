package com.tje.cinema.controllersREST;

import com.tje.cinema.domain.Reservation;
import com.tje.cinema.services.ReservationService;
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
@RequestMapping("/api/reservations")
public class RestReservationController {

    private final ReservationService reservationService;

    public RestReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservation(id);

        if (reservation != null) {
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reservation not found with id: " + id,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/order/{order_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getReservationsByOrderId(@PathVariable Long order_id) {
        return new ResponseEntity<> (reservationService.getReservationsByOrderId(order_id),HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addReservation(@RequestBody @Valid Reservation reservation){
        Reservation result = this.reservationService.addReservation(reservation);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editReservation(@RequestBody @Valid Reservation resUpdated, @PathVariable Long id){

        try{
            Reservation editedReservation = this.reservationService.editReservation(resUpdated);
            return new ResponseEntity<>(editedReservation,HttpStatus.OK) ;
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteReservationById(@PathVariable Long id) {
        try{
            Reservation reservation =  reservationService.getReservation(id);
            if(reservation == null){
                return new ResponseEntity<>("Reservation doesn't exist!",HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException ignored) {}
        reservationService.removeReservationById(id);
        return ResponseEntity.ok("Deleted reservation " + id);

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
