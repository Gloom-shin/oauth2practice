package oauth2.practice.oauth.service;

import lombok.RequiredArgsConstructor;
import oauth2.practice.api.entity.user.User;
import oauth2.practice.api.repository.UserRepository;
import oauth2.practice.oauth.entity.ProviderType;
import oauth2.practice.oauth.entity.RoleType;
import oauth2.practice.oauth.entity.UserPrincipal;
import oauth2.practice.oauth.exception.OAuthProviderMissMatchException;
import oauth2.practice.oauth.info.OAuth2UserInfo;
import oauth2.practice.oauth.info.OAuth2UserInfoFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        }catch (AuthenticationException ex) {// 인증관련 에러시
            throw ex;
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());

        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        // OAuth2 로그인 제공자인 서비스 구분 (구글, 네이버, 카카오 등)
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        // Factory Pattern 으로 구현된 OAuth2UserInfo 를 가져온다.
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        // OAuth2UserInfo 에서 가져온 정보로 DB 에 저장된 사용자인지 확인한다.
        User savedUser = userRepository.findByUserId(userInfo.getId());

        if(savedUser != null){
            if(providerType != savedUser.getProviderType()){ //소셜 로그인 제공자가 다를 경우
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedUser.getProviderType() +
                        " account to login.");
            }
            updateUser(savedUser, userInfo); // 같지만, 저장된 데이터가 다를 경우
        }else {
            //기존 로그인 사용자가 없음 -> 새로 갱신 시켜줌
             savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes()); // UserPrincipal 객체를 생성하여 반환
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType){
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                userInfo.getImageUrl(),
                providerType,
                RoleType.USER,
                now,
                now

        );
        return userRepository.saveAndFlush(user); // 영속성을 거치지않고 바로 저장

    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if(userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())){ // 이름이 변경되었을 경우
            user.setUsername(userInfo.getName()); // 이름 변경
        }
        if(userInfo.getImageUrl() != null && !user.getProfileImageUrl().equals(userInfo.getImageUrl())){ // 프로필 이미지가 변경되었을 경우
            user.setProfileImageUrl(userInfo.getImageUrl()); // 프로필 이미지 변경
        }

        return user;
    }

}
