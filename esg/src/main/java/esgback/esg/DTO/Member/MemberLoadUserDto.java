package esgback.esg.DTO.Member;

import esgback.esg.Domain.Enum.Sex;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class MemberLoadUserDto extends User implements OAuth2User {

    private String id;
    private String pwd;

    private Map<String, Object> props;//소셜 로그인 정보

    private String provider;
    private Boolean social;
    private Boolean success;

    public MemberLoadUserDto(String username, String pwd, String provider, Boolean social, Boolean success, Collection<GrantedAuthority> authorities) {
        super(username, pwd, authorities);
        this.id = username;
        this.pwd = pwd;
        this.provider = provider;
        this.social = social;//social 로그인이면서 회원 db에 없으면 계정 새로 파야됨
        this.success = success;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.id;
    }
}
