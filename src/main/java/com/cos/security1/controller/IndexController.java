package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다!!
public class IndexController {

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

  @GetMapping("/join")
  public String joinForm() {
    return "joinForm";
  }

  @GetMapping("/joinProc")
  public @ResponseBody String joinProc() {
    return "회원가입 완료됨!";
  }
}
