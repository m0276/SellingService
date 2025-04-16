package MJ.sellingservice.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class LoginUtil {

  public static boolean isLogin(){
    boolean result = true;

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(principal instanceof String){
      result = false;
    }

    return result;
  }
}
