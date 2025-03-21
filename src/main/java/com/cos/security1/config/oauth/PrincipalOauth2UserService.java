package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// 구글로 부터 받은 userRequest 데이터에 대한 후처리
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
      System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
      System.out.println("getTokenValue : " + userRequest.getAccessToken().getTokenValue()); // 엑세스 토큰을 받아올 수 있음
      System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes()); // 회원정보를 받아올 수 있음
      // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
      // -> userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필을 받아준다.
      
      OAuth2User oAuth2User = super.loadUser(userRequest);
      // 구글 로그인 시 회원가입을 자동 진행

      
      // 구글로부터 받은 회원정보
      return super.loadUser(userRequest);
  }
}
