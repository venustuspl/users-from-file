package pl.venustus.usersfromfile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.Exception.StoreException;
import pl.venustus.usersfromfile.helper.UserHelper;
import pl.venustus.usersfromfile.model.User;
import pl.venustus.usersfromfile.repository.UserRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Transactional
    public void save(MultipartFile file) {
        try {
            LOGGER.log(Level.INFO, "Started users saving");
            List<User> users = UserHelper.csvToUsers(file.getInputStream());
            for (User user : users) {
                if (!repository.findAllByPhoneNo(user.getPhoneNo()).isEmpty()) {
                    LOGGER.log(Level.WARNING, "Ended " + user + " will not be saved because his phone number exists in database");
                    continue;
                }
                repository.save(user);
                LOGGER.log(Level.INFO, "Ended " + user + " saving");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Ended saving, problems was found");
            throw new StoreException("Fail to store csv data: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Long getCountUsers() {
        return repository.count();
    }

    public List<User> getAllUsersSortByBirthDate() {
        return repository.findAllByOrderByBirthDateDesc();
    }

    public User getOldestUserWithPhoneNumber() {
        return repository.findAll().stream()
                .filter(user -> !user.getPhoneNo().isEmpty())
                .min(Comparator.comparing(User::getBirthDate))
                .orElseThrow(NoSuchElementException::new);
    }

    public List<User> getUserByLastName(String lastName) {
        return repository.findAll().stream()
                .filter(user -> user.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllUsers() {
        repository.deleteAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        repository.delete(repository.getById(id));
    }
}


