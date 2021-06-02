package pl.venustus.usersfromfile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.venustus.usersfromfile.repository.UserRepository;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FieldValidator {

    @Autowired
    private UserRepository userRepository;

    public boolean isPhoneNumberHasNineDigits(String phone_number) {
        return Pattern.matches("^\\d{9}$", phone_number);
    }
}
