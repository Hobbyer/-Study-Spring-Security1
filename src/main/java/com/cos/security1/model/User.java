package com.cos.security1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
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
}
