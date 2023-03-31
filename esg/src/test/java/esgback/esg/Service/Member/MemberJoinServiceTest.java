package esgback.esg.Service.Member;

import esgback.esg.DTO.MemberJoinDto;
import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Address;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

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

    @Test
    void joinMember() {
        Date date = new Date();

        Address address = Address.builder()
                .firstAddr("경기도")
                .secondAddr("수원시")
                .thirdAddr("영통구")
                .build();

        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .memberId("tester")
                .password("1234")
                .name("테스터")
                .nickname("테스터훈")
                .address(address)
                .sex(Sex.MAN)
                .birthDate(date)
                .phoneNumber("010-1234-1234")
                .build();

        memberJoinService.joinMember(memberJoinDto);

        Boolean test = memberJoinService.checkIdDuplicate("tester");

        Assertions.assertThat(test).isEqualTo(true);
    }
}