package com.cos.security1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor // 빈 생성자 생성 (default constructor)
public class User {
  @Id // primary key
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String username;
  private String password;
  private String email;
  private String role; // ROLE_USER, ROLE_ADMIN

  private String provider; // google(제공자)
  private String providerId; // 제공자로부터 받은 id

  @CreationTimestamp
  private Timestamp createDate;

  @Builder
  public User(String username, String password, String email, String role, String provider, String providerId,
      Timestamp createDate) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
    this.provider = provider;
    this.providerId = providerId;
    this.createDate = createDate;
  }
}
