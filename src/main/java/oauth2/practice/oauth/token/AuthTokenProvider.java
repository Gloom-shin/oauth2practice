package oauth2.practice.oauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import oauth2.practice.oauth.exception.TokenValidFailedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class AuthTokenProvider {
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(String secret) {
        // 암호학에서 Hmac은 암호화 해시함수와 기밀 암호화키를 수반하는 특정 유형의 메시지 인증코드이다.
        // SHA - 버전 등의 암호화 해시 함수는 HMAC 연산을 위해 사용할 수 있다.
        this.key = Keys.hmacShaKeyFor(secret.getBytes()); // 비밀키
    }
    public AuthToken createAuthToken(String id, Date expriry) {
        return new AuthToken(id, expriry, key);
    }
    public AuthToken createAuthToken(String id, String role, Date expriry) {
        return new AuthToken(id, role, expriry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }
    public Authentication getAuthentication(AuthToken authToken) { // 인증

        if(authToken.validate()){ // 토큰이 유효하다면
            Claims tokenClaims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{tokenClaims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .toList();

            log.debug("claims subject := [{}]", tokenClaims.getSubject());
            // 유저 정보를 담은 principal

            /**
             * 미완성
             */

            return new UsernamePasswordAuthenticationToken(authToken,authorities);
        }
        else {
            throw new TokenValidFailedException();
        }
    }
}
