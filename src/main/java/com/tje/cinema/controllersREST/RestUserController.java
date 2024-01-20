package com.tje.cinema.controllersREST;

import com.tje.cinema.domain.User;
import com.tje.cinema.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class RestUserController {

    private final UserService userService;

    public RestUserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found with id: " + id,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody @Valid User user){

        try{
            User foundUser = userService.getUserByEmail(user.getEmail());
            if (foundUser != null){
                return new ResponseEntity<>("User with this email already exists!",HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ignored){}


        User result = this.userService.saveUser(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@RequestBody @Valid User userUpdated, @PathVariable Long id){

        try{
            User editedUser = this.userService.editUser(userUpdated);
            return new ResponseEntity<>(editedUser,HttpStatus.OK) ;
        } catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {

        try{
           User user =  userService.getUserById(id);
            if(user == null){
                return new ResponseEntity<>("User doesn't exists!",HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException ignored) {}
        userService.deleteUserById(id);
        return ResponseEntity.ok("Deleted user with id: " + id);

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
