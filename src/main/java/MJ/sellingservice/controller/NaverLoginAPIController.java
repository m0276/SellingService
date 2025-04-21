package MJ.sellingservice.controller;

import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login/naver")
public class NaverLoginAPIController {

  private final UserRepository userRepository;
//https://notspoon.tistory.com/43
//  @GetMapping
//  public ResponseEntity<UserDto> getUserInfo(){
//
//  }
}
