package pl.venustus.usersfromfile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.helper.CSVHelper;
import pl.venustus.usersfromfile.model.User;
import pl.venustus.usersfromfile.service.CSVService;

import java.util.List;

@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    CSVService fileService;

    @RequestMapping("/")
    public String getAppPage() {
        return "/app.html";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
//            fileService.save(file);

            try {
                fileService.save(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body("\" message \": \" " + message + " \"");
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("\" message \": \" " + message + " \"");
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\" message \": \" " + message + " \"");
    }

    //1- SELECT * FROM Order;
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllOrders() {
        try {
            List<User> orders = fileService.getAllOrders();

            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<List<User>>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
