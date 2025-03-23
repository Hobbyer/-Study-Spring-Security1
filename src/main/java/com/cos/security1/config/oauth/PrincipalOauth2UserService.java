package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;


@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

  @Autowired 
  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  
  // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
  // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
  // 이 함수가 종료되면 @AuthenticationPrincipal 어노테이션이 만들어진다.
  // OAuth2UserRequest : OAuth2 로그인 요청에 대한 정보를 담는 객체
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
    System.out.println("getTokenValue : " + userRequest.getAccessToken().getTokenValue()); // 엑세스 토큰을 받아올 수 있음

    OAuth2User oAuth2User = super.loadUser(userRequest);

    // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
    // -> userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필을 받아준다.
    System.out.println("getAttributes : " + oAuth2User.getAttributes()); // 회원정보를 받아올 수 있음

    // 구글 로그인 시 자동 회원가입을 진행
    String provider = userRequest.getClientRegistration().getClientId(); // google
    String providerId = oAuth2User.getAttribute("sub"); // google의 id

    // username 과 password는 크게 의미 없음.
    String username = provider + "_" + providerId; // google_123456789
    // String password = bCryptPasswordEncoder.encode("겟인데어"); // 비밀번호는 암호화해서 DB에 저장

    String email = oAuth2User.getAttribute("email");
    String role = "ROLE_USER";

    // 이미 회원가입이 되어있는지 확인
    User userEntity = userRepository.findByUsername(username);

    if (userEntity == null) {
      System.out.println("OAuth 로그인이 최초입니다.");
      userEntity = User.builder()
          .username(username)
          // .password(password)
          .email(email)
          .role(role)
          .provider(provider)
          .providerId(providerId)
          .build();
      userRepository.save(userEntity);
    } else {
      System.out.println("이미 회원가입이 되어있습니다. 자동 로그인을 진행합니다.");
    }

    // 구글로부터 받은 회원정보
    return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
  }
}
