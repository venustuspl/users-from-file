package pl.venustus.usersfromfile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.helper.UserHelper;
import pl.venustus.usersfromfile.model.User;
import pl.venustus.usersfromfile.service.UserService;

import java.util.List;

@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/v1/")
public class UserController {

    private static final String MESSAGE = "\" Message \": \" ";
    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String getAppPage() {
        return "/app.html";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        var message = "";

        if (UserHelper.hasCSVFormat(file)) {
            try {
                userService.save(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(MESSAGE + message + " \"");
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(MESSAGE + message + " \"");
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MESSAGE + message + " \"");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/count")
    public ResponseEntity<Long> getAllUsersCount() {
        try {
            Long usersCount = userService.getCountUsers();

            return new ResponseEntity<>(usersCount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/birthdate")
    public ResponseEntity<List<User>> getAllUsersSortByBirthDate() {
        try {
            List<User> users = userService.getAllUsersSortByBirthDate();

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/birthdate/oldest")
    public ResponseEntity<User> getOldestUserWithPhoneNumber() {
        try {
            var user = userService.getOldestUserWithPhoneNumber();

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/lastname/{lastname}")
    public ResponseEntity<List<User>> getUserByLastName(@PathVariable String lastname) {
        try {
            List<User> users = userService.getUserByLastName(lastname);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{userid}/delete")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userid) {
        try {

            userService.deleteUser(userid);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/delete")
    public ResponseEntity<String> deleteAllUsers() {
        try {

            userService.deleteAllUsers();

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
