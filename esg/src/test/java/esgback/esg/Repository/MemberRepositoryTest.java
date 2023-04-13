package esgback.esg.Repository;

import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Address;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Member.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByPhoneNumber() {
        Address address = Address.builder()
                .firstAddr("경기도")
                .secondAddr("수원시")
                .thirdAddr("영통구").build();

        Member input = Member.builder()
                .memberId("isTest")
                .password("isTest")
                .name("isTester")
                .nickName("isTester")
                .role(Role.ROLE_USER)
                .address(address)
                .sex(Sex.MAN)
                .birthDate(LocalDate.parse("1999-01-23"))
                .phoneNumber("01013132525")
                .build();

        Member save = memberRepository.save(input);

        Member tester = memberRepository.findByPhoneNumber(save.getPhoneNumber());

        Assertions.assertThat(save.getMemberId()).isEqualTo(tester.getMemberId());
    }
}