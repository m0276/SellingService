package MJ.sellingservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home() {
    return "home";
  }

  @GetMapping("/signup")
  public String signupForm() {
    return "signup";
  }

  @GetMapping("/update")
  public String updateFrom() {
    return "update";
  }
}
