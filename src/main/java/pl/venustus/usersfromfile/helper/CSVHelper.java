package pl.venustus.usersfromfile.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.model.User;
import pl.venustus.usersfromfile.service.FieldValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVHelper {
    public static String TYPE = "application/vnd.ms-excel";
    static String[] HEADERs = {"first_name", "last_name", "birth_date", "phone_no"};
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.M.d");
    private static String test = "";

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<User> csvToUsers(InputStream is) {
         FieldValidator fieldValidator = new FieldValidator();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim().withDelimiter(';'));) {
            List<User> users = new ArrayList<User>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                System.out.println(csvRecord.get(0) + " " +
                        csvRecord.get(1) + " " +
                        csvRecord.get(2) + " " +
                        csvRecord.get(3)
                );
                if (!csvRecord.get(0).isEmpty() && !csvRecord.get(1).isEmpty() && !csvRecord.get(2).isEmpty()
                        && fieldValidator.isPhoneNumberHasNineDigits(csvRecord.get(3))                ) {
                    User user = new User(
                            csvRecord.get(0),
                            csvRecord.get(1),
                            LocalDate.parse(!csvRecord.get(2).isEmpty() ? csvRecord.get(2) : "1900.01.01", dateFormatter),
                            csvRecord.get(3)
                    );
                    users.add(user);
                }
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
