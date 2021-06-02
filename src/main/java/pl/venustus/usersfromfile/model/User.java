package pl.venustus.usersfromfile.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String first_name;

    @NotNull
    private String last_name;

    @NotNull
    private LocalDate birth_date;

    @NotNull
    private String phone_no;

    public User(String first_name, String last_name, LocalDate birth_date, String phone_no) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.phone_no = phone_no;
    }
}
