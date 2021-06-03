package pl.venustus.usersfromfile.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.venustus.usersfromfile.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTestIT {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    MultipartFile multipartFile;

    @AfterEach
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveUserInDatabase() {
        //given
        multipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "first_name;last_name;birth_date;phone_no\nStefan;Testowy;1988.11.11;600700800".getBytes()
        );
        //when
        userService.save(multipartFile);
        //then
        assertThat(userRepository.findById(1L).get().getFirstName()).isEqualTo("Stefan");
    }
}