package pl.venustus.usersfromfile.service;

import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class UserFieldValidator {
    private static final Logger LOGGER = Logger.getLogger(UserFieldValidator.class.getName());

    public boolean isPhoneNumberHasNineDigits(String phoneNumber) {
        return Pattern.matches("^\\d{9}$", phoneNumber);
    }

    public boolean isCSVRecordValidForSave(String firstName, String lastName, String birthDate, String phoneNumber) {
        var logMessage = new StringBuilder();
        if (firstName.isEmpty()) {
            logMessage.append(" first_name is empty");
        }
        if (lastName.isEmpty()) {
            logMessage.append(" last_name is empty");
        }
        if (birthDate.isEmpty()) {
            logMessage.append(" birth_date is empty");
        }
        if (!phoneNumber.isEmpty() && !Pattern.matches("^\\d{9}$", phoneNumber)) {
            logMessage.append(" phone_no is not empty and hasn't 9 digits");
        }
        if (logMessage.length() != 0) {
            LOGGER.log(Level.WARNING, "Row: " + firstName + " " + lastName + " " + birthDate + " " + phoneNumber + " will not be saved, because " + logMessage);
            return false;
        }
        return true;
    }
}