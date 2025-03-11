package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

  // localhost:8080/
  // localhost:8080
  @GetMapping({"", "/"})
  public String index() {
    // 머스테치(mustache) 기본폴더 : src/main/resources/
    // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) - 생략가능!!
    return "index"; // src.main.resources.templates/index.mustache
  }

  @GetMapping("/admin")
  public String admin() {
    return "admin";
  }

  @GetMapping("/manager")
  public String manager() {
    return "manager";
  }

  @GetMapping("/login")
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

    return "redirect:/login";
  }

  @GetMapping("/joinProc")
  public @ResponseBody String joinProc() {
    return "회원가입 완료됨!";
  }
}
