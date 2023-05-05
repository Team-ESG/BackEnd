package esgback.esg.Security;

import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> find = memberRepository.findByMemberId(username);

        Member member = find.orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 존재하지 않습니다."));


        MemberLoadUserDto memberLoadUserDto = new MemberLoadUserDto(
                member.getMemberId(),
                member.getPassword(),
                "nothing",
                false,
                true,
                List.of(new SimpleGrantedAuthority(member.getRole().name())
                ));

        return memberLoadUserDto;
    }

}
