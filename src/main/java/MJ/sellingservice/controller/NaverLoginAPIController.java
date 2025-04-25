package MJ.sellingservice.controller;

import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.repository.UserRepository;
import MJ.sellingservice.service.NaverAPIService;
import MJ.sellingservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class NaverLoginAPIController {
    private final NaverAPIService naverAPIService;

  @GetMapping("/oauth/naver")
  public ResponseEntity<Void> redirectToNaverLogin(HttpSession session) {
      String state = UUID.randomUUID().toString();
      session.setAttribute("naver_oauth_state", state);

      String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8);
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create(naverAPIService.makeLoginURL(encodedState)));
      return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }


  @GetMapping("/oauth/naver/callback")
  public ResponseEntity<String> handleNaverCallback(
      @RequestParam("code") String code,
      @RequestParam("state") String state,
      HttpSession session
    ) {
    String savedState = (String) session.getAttribute("naver_oauth_state");

    if (savedState == null || !savedState.equals(state)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Invalid state parameter. Possible CSRF attack.");
    }

    session.removeAttribute("naver_oauth_state");

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(
        naverAPIService.getToken(code, state), String.class
    );

    return ResponseEntity.ok(response.getBody());
  }

  @GetMapping("/oauth/naver/userinfo")
  public ResponseEntity<String> getUserInfo(@RequestParam("access_token") String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.set("Content-Type", "application/x-www-form-urlencoded");

    HttpEntity<String> entity = new HttpEntity<>("", headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.exchange(
        "https://openapi.naver.com/v1/nid/me",
        HttpMethod.GET,
        entity,
        String.class
    );

    return ResponseEntity.ok(response.getBody());
  }
}
