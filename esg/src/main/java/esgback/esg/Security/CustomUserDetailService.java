package esgback.esg.Security;

import esgback.esg.DTO.Member.MemberLoadUserDto;
import esgback.esg.Domain.Market.Market;
import esgback.esg.Domain.Member.Member;
import esgback.esg.Repository.MarketRepository;
import esgback.esg.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberLoadUserDto memberLoadUserDto = null;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(username.contains("@timeseller")){
            Optional<Market> findOwner = marketRepository.findByEmail(username);
            Market market = findOwner.orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 존재하지 않습니다."));

            memberLoadUserDto = new MemberLoadUserDto(
                    market.getEmail(),
                    passwordEncoder.encode(market.getPassword()),
                    "nothing",
                    false,
                    true,
                    List.of(new SimpleGrantedAuthority("ROLE_OWNER")
                    ));
        }
        else{
            Optional<Member> findMember = memberRepository.findByMemberId(username);
            Member member = findMember.orElseThrow(() -> new UsernameNotFoundException("해당 아이디는 존재하지 않습니다."));
            System.out.println(member.getRole().name());
            memberLoadUserDto = new MemberLoadUserDto(
                    member.getMemberId(),
                    member.getPassword(),
                    "nothing",
                    false,
                    true,
                    List.of(new SimpleGrantedAuthority(member.getRole().name())
                    ));
        }

        return memberLoadUserDto;
    }

}
