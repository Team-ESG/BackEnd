package esgback.esg.Service.Member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    void lengthFailTest() {
        String lengthFailCase = "0311234567";

        String fail = messageService.sendOneMsg(lengthFailCase);

        org.assertj.core.api.Assertions.assertThat(fail).isEqualTo("전화번호 길이가 너무 짧습니다.");
    }

    @Test
    void charFailTest() {
        String charFailCase = "010-1234-5678";

        String fail = messageService.sendOneMsg(charFailCase);

        org.assertj.core.api.Assertions.assertThat(fail).isEqualTo("전화번호 길이가 너무 깁니다.");
    }

    @Test
    void successTest() {
        String number = "01056781234";
        String result = messageService.sendOneMsg(number);

        boolean success = (result.length() == 4);

        Assertions.assertTrue(success);
    }
}
