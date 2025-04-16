package MJ.sellingservice.service;


import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.exception.AlreadyJoinException;
import MJ.sellingservice.exception.HaveToLoginException;
import MJ.sellingservice.exception.NoCorrectPasswordException;
import MJ.sellingservice.exception.NoJoinUserException;
import MJ.sellingservice.mapper.UserMapper;
import MJ.sellingservice.repository.UserRepository;
import MJ.sellingservice.service.common.CommonMethod;
import MJ.sellingservice.util.LoginUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    userRepository.save(user);
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

    if(userRepository.findByEmail(email).isEmpty()) throw new NoJoinUserException("해당 하는 계정이 없습니다.");

    return userMapper.toDto(userRepository.findByEmail(email).get());
  }
}
