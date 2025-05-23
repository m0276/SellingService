package MJ.sellingservice.controller;

import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.NaverUserInfo;
import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.repository.UserRepository;
import MJ.sellingservice.service.NaverAPIService;
import MJ.sellingservice.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
    private final UserService userService;

  @GetMapping("/oauth/naver")
  public ResponseEntity<Void> redirectToNaverLogin(HttpSession session) {
      String state = UUID.randomUUID().toString();
      session.setAttribute("naver_oauth_state", state);

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create(naverAPIService.makeLoginURL(state)));
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
        naverAPIService.getToken(code, state),
        String.class
    );

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode tokenJson = objectMapper.readTree(response.getBody());
      String accessToken = tokenJson.get("access_token").asText();

      NaverUserInfo userInfo = userService.getUserInfo(accessToken);

      User user = userService.saveOrUpdateNaverUser(userInfo);
      session.setAttribute("LOGIN_USER", user);

      UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
          .username(user.getEmail())
          .password(user.getPassword())
          .authorities("ROLE_USER")
          .build();

      Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);

      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          SecurityContextHolder.getContext()
      );

      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create("/"));
      return new ResponseEntity<>(headers, HttpStatus.FOUND);

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 파싱 실패");
    }
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
