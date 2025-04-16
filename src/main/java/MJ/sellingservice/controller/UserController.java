package MJ.sellingservice.controller;

import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserDto> getUserInfo(){
    return ResponseEntity.status(HttpStatus.OK).body(userService.get());
  }

  @PostMapping("/join")
  public ResponseEntity<UserDto> joinUser(@RequestBody UserRequest userRequest){
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequest));
  }

  @PatchMapping("/me")
  public ResponseEntity<UserDto> updateUser(@RequestBody UserRequest userRequest){
    return ResponseEntity.status(HttpStatus.OK).body(userService.update(userRequest));
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteUser(@RequestBody UserRequest userRequest){
    userService.delete(userRequest);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
