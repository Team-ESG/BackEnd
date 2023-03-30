package esgback.esg.Service.Member;

import esgback.esg.Repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberJoinServiceTest {

    @Autowired
    private MemberJoinService memberJoinService;

    @Test
    void checkIdDuplicate() {
        Boolean success = memberJoinService.checkIdDuplicate("user1");
        Boolean fail = memberJoinService.checkIdDuplicate("user0");

        Assertions.assertThat(success).isEqualTo(true); // 동일한 값이 있어야 함
        Assertions.assertThat(fail).isEqualTo(false); // 동일한 값이 없어야 함

    }

    @Test
    void checkNickNameDuplicate() {
        Boolean success = memberJoinService.checkNickNameDuplicate("hong123");
        Boolean fail = memberJoinService.checkNickNameDuplicate("dong123");

        Assertions.assertThat(success).isEqualTo(true); // 동일한 값이 있어야 함
        Assertions.assertThat(fail).isEqualTo(false); // 동일한 값이 있어야 함
    }
}