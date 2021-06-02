package pl.venustus.usersfromfile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.helper.CSVHelper;
import pl.venustus.usersfromfile.model.User;
import pl.venustus.usersfromfile.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVService {

    @Autowired
    UserRepository repository;

    public void save(MultipartFile file) {
        try {
            List<User> users = CSVHelper.csvToUsers(file.getInputStream());
            repository.saveAll(users);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<User> getAllOrders() {
        return repository.findAll();
    }


    public List<User> getOrdersByUserName(String UserName) {

        return repository.findAll().stream()
                .filter(s -> s.getFirst_name().contentEquals(UserName))
                .collect(Collectors.toList());
    }

    public List<User> getOrdersByUserNameAndDate(String name, String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate dateFormat = LocalDate.parse(date, formatter);

        return repository.findAll().stream()
                .filter(s -> s.getFirst_name().contentEquals(name))
                .filter(s -> s.getBirth_date().isEqual(LocalDate.from(dateFormat)))
                .collect(Collectors.toList());
    }
}


