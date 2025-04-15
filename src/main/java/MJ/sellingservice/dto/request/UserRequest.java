package MJ.sellingservice.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserRequest {
  String username;
  String password;

  @Email
  String email;

  Boolean favorite;
}
