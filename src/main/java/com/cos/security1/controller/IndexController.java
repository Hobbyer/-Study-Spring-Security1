package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다!!
public class IndexController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  // 일반 로그인
  @GetMapping("/test/login")
  public @ResponseBody String loginTest(Authentication authentication,
                                        // Authentication 객체로 세션에 접근할 수 있다.
                                        @AuthenticationPrincipal PrincipalDetails userDetails) { // DI(의존성 주입)
                                      // @AuthenticationPrincipal 어노테이션을 사용하면 PrincipalDetails 타입으로 받을 수 있다. 
    System.out.println("test/login =============================");
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // PrincipalDetails 타입으로 캐스팅
    System.out.println("authentication : " + principalDetails.getUser());

    System.out.println("userDetails : " + userDetails.getUsername());

    return "세션 정보 확인하기";
  }

  // OAuth 로그인
  @GetMapping("/test/oauth/login")
  public @ResponseBody String loginTest(Authentication authentication,
                                        @AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
    System.out.println("test/oauth/login =============================");
    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    System.out.println("authentication : " + oauth2User.getAttributes());

    System.out.println("oauth2User : " + oauth.getAttributes());

    return "OAuth 세션 정보 확인하기";
  }

  // localhost:8080/
  // localhost:8080
  @GetMapping({"", "/"})
  public String index() {
    // 머스테치(mustache) 기본폴더 : src/main/resources/
    // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) - 생략가능!!
    return "index"; // src.main.resources.templates/index.mustache
  }

  @GetMapping("/admin")
  public @ResponseBody String admin() {
    return "admin";
  }

  @GetMapping("/manager")
  public @ResponseBody String manager() {
    return "manager";
  }

  @GetMapping("/user")
  public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    System.out.println("principalDetails : " + principalDetails.getUser());
    return "user";
  }

  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

  @GetMapping("/joinForm")
  public String joinForm() {
    return "joinForm";
  }

  @PostMapping("/join")
  public String join(User user) {
    user.setRole("ROLE_USER");
    String rawPassword = user.getPassword();
    String encPassWord = bCryptPasswordEncoder.encode(rawPassword);
    user.setPassword((encPassWord));
    System.out.println(user);
    userRepository.save(user); // 회원가입 잘됨. 비밀번호 : 1234 => 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화가 안되었기 때문!!

    return "redirect:/loginForm";
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/info")
  public @ResponseBody String info() {
    return "개인정보";
  }

  // @PostAuthorize("hasRole('ROLE_ADMIN')")
  @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
  @GetMapping("/data")
  public @ResponseBody String data() {
    return "데이터정보보";
  }

  @GetMapping("/joinProc")
  public @ResponseBody String joinProc() {
    return "회원가입 완료됨!";
  }
}
