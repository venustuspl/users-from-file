package pl.venustus.usersfromfile.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.Exception.ParseException;
import pl.venustus.usersfromfile.model.User;
import pl.venustus.usersfromfile.service.UserFieldValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserHelper {
    public static final String TYPE = "application/vnd.ms-excel";
    static String[] HEADERs = {"first_name", "last_name", "birth_date", "phone_no"};
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.M.d");

    private UserHelper() {
    }

    public static boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<User> csvToUsers(InputStream is) {
        var userFieldValidator = new UserFieldValidator();
        var rowNumber = 1;

        try (var fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             var csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';'))) {

            List<User> users = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                if (userFieldValidator.isCSVRecordValidForSave(rowNumber, csvRecord.get(0), csvRecord.get(1), csvRecord.get(2), csvRecord.get(3))) {
                    var user = new User(
                            csvRecord.get(0),
                            csvRecord.get(1),
                            LocalDate.parse(csvRecord.get(2), dateFormatter),
                            csvRecord.get(3)
                    );
                    users.add(user);
                }
                rowNumber++;
            }
            return users;
        } catch (IOException e) {
            throw new ParseException("Fail to parse CSV file: " + e.getMessage());
        }
    }
}
