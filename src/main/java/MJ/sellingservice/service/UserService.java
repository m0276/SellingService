package MJ.sellingservice.service;


import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.NaverUserInfo;
import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.exception.custom.AlreadyJoinException;
import MJ.sellingservice.exception.custom.HaveToLoginException;
import MJ.sellingservice.exception.custom.NoCorrectPasswordException;
import MJ.sellingservice.exception.custom.NoJoinUserException;
import MJ.sellingservice.mapper.UserMapper;
import MJ.sellingservice.repository.UserRepository;
import MJ.sellingservice.service.common.CommonMethod;
import MJ.sellingservice.util.LoginUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements CommonMethod<UserDto, UserRequest> {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder encoder;


  @Override
  public UserDto save(UserRequest request) {
    if(userRepository.findByEmail(request.getEmail()).isPresent()) throw new AlreadyJoinException("이미 가입되어 있습니다.");

    User user = new User(request.getUsername(), encoder.encode(request.getPassword()), request.getEmail());
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto update(UserRequest request) {
    if(!LoginUtil.isLogin()) throw new HaveToLoginException("로그인이 필요합니다.");
    String email = LoginUtil.getCurrentUserEmail();

    if(userRepository.findByEmail(email).isEmpty()) throw new NoJoinUserException("해당 하는 계정이 없습니다.");

    User user = userRepository.findByEmail(email).get();
    user.setPassword(encoder.encode(request.getPassword()));

    return userMapper.toDto(user);
  }

  @Override
  public void delete(UserRequest request) {
    if(!LoginUtil.isLogin()) throw new HaveToLoginException("로그인이 필요합니다.");

    String email = LoginUtil.getCurrentUserEmail();

    if(userRepository.findByEmail(email).isEmpty()) throw new NoJoinUserException("해당 하는 계정이 없습니다.");

    User user = userRepository.findByEmail(email).get();
    if (encoder.matches(request.getPassword(), user.getPassword())) {
      userRepository.delete(user);
    } else {
      throw new NoCorrectPasswordException("비밀번호가 맞지 않습니다.");
    }
  }

  @Override
  public UserDto get() {
    if(!LoginUtil.isLogin()) throw new HaveToLoginException("로그인이 필요합니다.");
    String email = LoginUtil.getCurrentUserEmail();

    if(userRepository.findByEmail(email).isEmpty()) throw new NoJoinUserException("해당하는 계정이 없습니다.");

    return userMapper.toDto(userRepository.findByEmail(email).get());
  }

  User findByUserEmail(String email){
    return userRepository.findByEmail(email).orElseThrow(() -> new NoJoinUserException("해당하는 유저가 없습니다."));
  }

  private final RestTemplate restTemplate = new RestTemplate(); // 또는 @Bean으로 등록
  private final ObjectMapper objectMapper = new ObjectMapper();

  public NaverUserInfo getUserInfo(String accessToken) {
    String url = "https://openapi.naver.com/v1/nid/me";

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> entity = new HttpEntity<>("", headers);

    ResponseEntity<String> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        String.class
    );

    try {
      JsonNode jsonNode = objectMapper.readTree(response.getBody());
      JsonNode responseNode = jsonNode.get("response");

      return NaverUserInfo.builder()
          .id(responseNode.get("id").asText())
          .email(responseNode.get("email").asText(null))
          .name(responseNode.get("name").asText(null))
          .build();

    } catch (Exception e) {
      throw new RuntimeException("Failed to parse user info from Naver", e);
    }
  }

  public User saveOrUpdateNaverUser(NaverUserInfo userInfo) {
    Optional<User> existing = userRepository.findByProviderAndProviderId("naver", userInfo.getId());

    if (existing.isPresent()) {
      return existing.get();
    }


    return existing
        .orElseGet(() -> {
          User newUser = new User();
          newUser.setEmail(userInfo.getEmail());
          newUser.setPassword("naverUser");
          newUser.setUsername(userInfo.getName());
          newUser.setProvider("naver");
          newUser.setProviderId(userInfo.getId());
          return userRepository.save(newUser);
        });
  }
}
