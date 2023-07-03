package oauth2.practice.oauth.token;

import io.jsonwebtoken.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    AuthToken(String id, Date expiry, Key key) {
        this.token = createAuthToken(id, expiry);
        this.key = key;
    }
    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }


    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder() // 토큰 생성
                .setSubject(id)//토큰 제목 설정
                .signWith(key, SignatureAlgorithm.HS256) // 비밀키
                .setExpiration(expiry) // 만료 시간 설정
                .compact();
    }
    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role) // 권한 부여
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                .setExpiration(expiry) // 만료 시간 설정
                .compact();

    }

    public boolean validate(){
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }


    // 무슨 메소드이지?
    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()//토큰값을 파싱하고 build하여 인스턴스 만듬.
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) //토큰값을 파싱하여 Claims를 반환
                    .getBody(); //Claims를 반환
        } catch (ExpiredJwtException e) { //만료된 토큰일 경우
            log.info("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }
}
