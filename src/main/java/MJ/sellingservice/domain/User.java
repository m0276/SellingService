package MJ.sellingservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

  @Id
  Long id;

  String username;
  String password;
  String email;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public User() {

  }
}
