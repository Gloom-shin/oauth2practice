package oauth2.practice.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import oauth2.practice.api.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails, OidcUser, OAuth2User {
    // OidcUser 에 OAuth2User가 상속받고 있다.
    // UserDetail은 간단하게, 사용자 정보를 담는 인터페이스이다.
            // 계정의 권한 목록, 계정 비밀번호, 고유ID(이메일), 계정의 만료여부, 계정의 잠김여부, 계정의 비밀번호 만료여부, 계정의 활성화 여부
    // OidcUser는 OpenID Connect 1.0 Provider에서 사용자 정보를 가져오는 인터페이스이다.
    // OAuth2User는 OAuth 2.0 Provider에서 사용자 정보를 가져오는 인터페이스이다.
             // 이름, 중간이름, 성, 이메일, 전화번호, 주소등이 해당된다.
    private final String userId;
    private final String password;
    private final ProviderType providerType;
    private final RoleType roleType;
    private final Collection<GrantedAuthority> authorities; // 계정에 부여된 권한 목록
    private Map<String, Object> attributes;// 속성
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { // 고유 ID (이메일)
        // 이메일로 되어 있는 경우 SSO 같은 서버때 중복될 수 있다.
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() { // 계정이 만료되지 않았는지
        return true;
    }// 계정이 만료되지 않았는지

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠기지 않았는지
        return true;
    }// 계정이 잠기지 않았는지

    @Override
    public boolean isCredentialsNonExpired() { // 계정의 비밀번호가 만료되지 않았는지
        return true;
    } // 계정의 비밀번호가 만료되지 않았는지

    @Override
    public boolean isEnabled() { // 계정이 활성화 되었는지
        return true;
    }// 계정이 활성화 되었는지

    @Override
    public String getName() {
        return userId;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }


    public static UserPrincipal create(User user) {
        return new UserPrincipal(
                user.getUserId(),
                user.getPassword(),
                user.getProviderType(),
                RoleType.USER,
                Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.getCode()))
        );
    }
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }
}
