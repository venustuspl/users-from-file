package pl.venustus.usersfromfile.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserFieldValidatorTest {
    private UserFieldValidator userFieldValidator;

    @BeforeEach
    void setUp() {
        userFieldValidator = new UserFieldValidator();
    }

    @Test
    void shouldNotAcceptEightDigitNumber() {
        //given
        String phoneNumber = "12345678";
        //when
        //then
        assertThat(userFieldValidator.isPhoneNumberHasNineDigits(phoneNumber)).isFalse();
    }

    @Test
    void shouldAcceptUserRowValues() {
        //given
        String firstName = "testfirstname";
        String lastName = "testlastname";
        String birthDate = "2001.09.11";
        String phoneNumber = "123456789";
        //when
        //then
        assertThat(userFieldValidator.isCSVRecordValidForSave(firstName, lastName, birthDate, phoneNumber)).isTrue();
    }
}