package esgback.esg.Security;

import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class TryUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> find = memberRepository.findByMemberId(username);

        Member member = find.orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 존재하지 않습니다."));

        MemberLoadUserDto memberLoadUserDto = MemberLoadUserDto.builder()
                .username(member.getMemberId())
                .pwd(member.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        return memberLoadUserDto;
    }

}
