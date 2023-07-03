package oauth2.practice.oauth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{
    // 참고 링크
    //https://velog.io/@crow/AuthenticationEntryPoint-%EC%9D%B4%ED%95%B4
    // AuthenticationEntryPoint는 인증예외가 발생했을 떄 의미가 있는 클래스
    // 기본적으로 spring security 초기화후 2개의 endpoint가 존재한다.

    @Override //commence: 시작하다
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        log.info("Responding with unauthorized error. Message - {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
    }
}
