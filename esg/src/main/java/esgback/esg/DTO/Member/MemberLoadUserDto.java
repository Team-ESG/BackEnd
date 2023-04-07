package esgback.esg.DTO.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MemberLoadUserDto extends User {

    private String id;
    private String pwd;

    @Builder
    public MemberLoadUserDto(String username, String pwd, Collection<GrantedAuthority> authorities) {
        super(username, pwd, authorities);
        this.id = username;
        this.pwd = pwd;
    }
}
