package esgback.esg.Security;

import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.Domain.Enum.Sex;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Domain.Member.Role;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String[] specialChar = new String[]{"!", "@", "#", "$", "%", "^", "&", "&", "(", ")"};

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName(); //소셜 로그인 제공 업체

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;

        switch (clientName) {

            case "kakao":
                email = getKaKaoEmail(attributes);
                break;

            case "Naver":
                email = getNaverEmail(attributes);
                break;

        }

        MemberLoadUserDto memberLoadUserDto = generateDto(clientName, email, attributes);


        return memberLoadUserDto;
    }

    private String getKaKaoEmail(Map<String, Object> attributes) {
        Object kakao_account = attributes.get("kakao_account");

        LinkedHashMap orderedData = (LinkedHashMap) kakao_account;

        String email = (String) orderedData.get("email");

        return email;
    }

    private String getNaverEmail(Map<String, Object> attributes) {
        Object naver_account = attributes.get("response");

        LinkedHashMap orderedData = (LinkedHashMap) naver_account;

        String email = (String) orderedData.get("email");

        return email;
    }

    private MemberLoadUserDto generateDto(String clientName, String email, Map<String, Object> attributes) {
        boolean isMember = memberRepository.existsByMemberId(email);

        String name = null;
        String nickName = null;
        String encodePassword = null;
        String birthday;
        LocalDate birthdate = null;
        Sex sex = null;

        if (!isMember) {
            if(clientName.equals("kakao")){
                Object kakao_account = attributes.get("kakao_account");
                LinkedHashMap orderedData = (LinkedHashMap) kakao_account;

                LinkedHashMap<String, String> profile = (LinkedHashMap<String, String>)orderedData.get("profile");

                sex = ((orderedData.get("gender").equals("male")) ? Sex.MAN : Sex.WOMAN);
                name = profile.get("nickname");

                encodePassword = passwordEncoder.encode(generatePassword());//임의의 비밀번호 생성

                String randNum = Integer.toString((int) (Math.random() * 10000));

                nickName = name + randNum;

                birthday = (String) orderedData.get("birthday");
                int month = Integer.parseInt(birthday.substring(0, 2));
                int day = Integer.parseInt(birthday.substring(2));
                int year = 1900;
                birthdate = LocalDate.of(year, month, day);
            }
            else if (clientName.equals("Naver")) {
                Object naver_account = attributes.get("response");
                LinkedHashMap<String, String> orderedData = (LinkedHashMap<String, String>) naver_account;

                sex = ((orderedData.get("gender")).equals("M")) ? Sex.MAN : Sex.WOMAN;
                name = orderedData.get("name");

                encodePassword = passwordEncoder.encode(generatePassword());//임의의 비밀번호 생성

                boolean isNickName = memberRepository.existsByNickName(orderedData.get("nickname"));
                if (isNickName) {//nickname 있을 때,
                    String randNum = Integer.toString((int) (Math.random() * 10000));
                    nickName = orderedData.get("nickname") + randNum;
                }
                else{//nickname 없을 때,
                    nickName = orderedData.get("nickname");
                }
                birthday = orderedData.get("birthday");
                int month = Integer.parseInt(birthday.substring(0, 2));
                int day = Integer.parseInt(birthday.substring(3));
                int year = 1900;
                birthdate = LocalDate.of(year, month, day);
            }

            Member member = Member.builder()
                    .memberId(email)
                    .password(encodePassword)
                    .name(name)
                    .nickName(nickName)
                    .role(Role.ROLE_USER)
                    .sex(sex)
                    .birthDate(birthdate)
                    .social(true)
                    .build();

            memberRepository.save(member);

            MemberLoadUserDto memberLoadUserDto = new MemberLoadUserDto(email, encodePassword, member.getSocial(), Arrays.asList(new SimpleGrantedAuthority(Role.ROLE_USER.name())));
            memberLoadUserDto.setProps(attributes);

            return memberLoadUserDto;
        }
        else{
            Member member = memberRepository.findByMemberId(email).get();

            MemberLoadUserDto memberLoadUserDto = new MemberLoadUserDto(member.getMemberId(), member.getPassword(), member.getSocial(), Arrays.asList(new SimpleGrantedAuthority(member.getRole().name())));

            return memberLoadUserDto;
        }
    }

    private String generatePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            password.append(random.nextInt(10));
        }

        for (int i = 0; i < 2; i++) {
            password.append(specialChar[random.nextInt(10)]);
        }

        return String.valueOf(password);
    }
}
