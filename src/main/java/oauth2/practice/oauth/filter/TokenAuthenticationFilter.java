package oauth2.practice.oauth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oauth2.practice.oauth.token.AuthToken;
import oauth2.practice.oauth.token.AuthTokenProvider;
import oauth2.practice.utils.HeaderUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String tokenStr = HeaderUtil.getAccessToken(request);// 헤더에서 access 토큰 추출
        AuthToken authToken = tokenProvider.convertAuthToken(tokenStr); // 인증 토큰 생성

        if(authToken.validate()){
            Authentication authentication = tokenProvider.getAuthentication(authToken);
            // [SecurityContextHolder]에 인증 정보 저장
            // [principal]은 [Authentication]에서 관리하고, [Authentication]은 [SecurityContext]에서 관리한다.
            // [SecurityContext]는 [SecurityContextHolder]에서 관리한다.
            // 기본적으로 TreadLocal 을 사용하여, 한쓰레드안에서 Authentication 을 공유한다.
            // 즉, 쓰레드가 달라지면 제대로 된 인증정보를 가져올 수가 없다.

            SecurityContextHolder.getContext().setAuthentication(authentication); // securityContext 에서 인증 상태를 저장하는 것이다.
        }
        filterChain.doFilter(request, response);
    }
}
