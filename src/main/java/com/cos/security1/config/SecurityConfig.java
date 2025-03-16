package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// @Secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig {

  // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
  @Bean
  public BCryptPasswordEncoder encodePwd(){
    return new BCryptPasswordEncoder();
  }

  // 스프링부트 3.x 버전부터 SecurityConfig에 대한 내용이 많이 바뀜.
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/user/**").authenticated() // 로그인 인증만 되면 들어갈 수 있는 주소!!
            .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")// manager 로 들어오는 MANAGER 인증 또는 ADMIN 인증이 필요하다는 뜻이다.
            .requestMatchers("/admin/**").hasRole("ADMIN") // //admin으로 들어오면 ADMIN권한이 있는 사람만 들어올 수 있음
            .anyRequest().permitAll() // 그리고 나머지 url은 전부 권한을 허용해준다.
        );

    http.formLogin(form -> form
        .loginPage("/loginForm")
        .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
        .defaultSuccessUrl("/")
    );

    http.oauth2Login(oauth2 -> oauth2 // oauth2 로그인을 사용하겠다는 뜻
        .loginPage("/loginForm") // 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 코드X, (엑세스토큰+사용자프로필정보 O)
        // .userInfoEndpoint(userInfo -> userInfo // oauth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들
        //     .userService(null) // 소셜로그인 성공 이후 후처리가 필요함. 구글 로그인하고 나서 후처리를 해야함
        // )
    );
    return http.build();
  }
}