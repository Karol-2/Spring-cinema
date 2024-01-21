package com.tje.cinema.controllersREST;

import com.tje.cinema.domain.Order;
import com.tje.cinema.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class RestOrderController {

    private final OrderService orderService;

    public RestOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrder(id);

        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Order not found with id: " + id,HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody @Valid Order order){
        Order result = this.orderService.addOrder(order);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable Long id) {
        try{
            Order order =  orderService.getOrder(id);
            if(order == null){
                return new ResponseEntity<>("Order doesn't exist!",HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException ignored) {}
        orderService.removeOrderById(id);
        return ResponseEntity.ok("Deleted order with id: " + id);

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
