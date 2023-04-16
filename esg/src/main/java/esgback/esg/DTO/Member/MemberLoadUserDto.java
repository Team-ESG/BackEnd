package esgback.esg.DTO.Member;

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
    private Boolean isExist;
    private Boolean social;

    public MemberLoadUserDto(String username, String pwd, Boolean isExist, Boolean social, Collection<GrantedAuthority> authorities) {
        super(username, pwd, authorities);
        this.id = username;
        this.pwd = pwd;
        this.isExist = isExist;
        this.social = social;//social 로그인이면서 회원 db에 없으면 계정 새로 파야됨
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
