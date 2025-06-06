package MJ.sellingservice.util;

import MJ.sellingservice.exception.custom.HaveToLoginException;
import java.util.NoSuchElementException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


@Component
public class LoginUtil {

  public static boolean isLogin(){
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.isAuthenticated() &&
        !(authentication instanceof AnonymousAuthenticationToken);
  }

  public static String getCurrentUserEmail() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
      return userDetails.getUsername();
    }

    throw new IllegalStateException("현재 로그인된 사용자가 없습니다.");
  }
}
