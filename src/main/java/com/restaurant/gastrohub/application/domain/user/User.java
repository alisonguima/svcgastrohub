package com.restaurant.gastrohub.application.domain.user;


import com.restaurant.gastrohub.application.domain.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  private Long id;
  private String name;
  private String email;
  private String login;
  private String password;
  private UserType userType;
  private String address;
  private String lastUpdateAt;

}
