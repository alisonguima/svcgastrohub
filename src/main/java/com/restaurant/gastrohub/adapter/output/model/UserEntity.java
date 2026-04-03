package com.restaurant.gastrohub.adapter.output.model;

import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.util.DateTimeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserType userType;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private ZonedDateTime lastUpdateAt;

  private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

  @PrePersist
  protected void onCreate() {
    this.password = PASSWORD_ENCODER.encode(this.password);
    this.lastUpdateAt = DateTimeUtils.generateDateTimeZoneUTC();
  }

  @PreUpdate
  protected void onUpdate() {
    this.lastUpdateAt = DateTimeUtils.generateDateTimeZoneUTC();
  }
}
