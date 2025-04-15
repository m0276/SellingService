package MJ.sellingservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "user_name")
  String username;

  @Column(name = "password")
  String password;

  @Column(name = "email")
  String email;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
