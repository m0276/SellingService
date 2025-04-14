package MJ.sellingservice.service;


import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.UserDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.exception.AlreadyJoinException;
import MJ.sellingservice.exception.NoCorrectPasswordException;
import MJ.sellingservice.exception.NoJoinUserException;
import MJ.sellingservice.mapper.UserMapper;
import MJ.sellingservice.repository.UserRepository;
import MJ.sellingservice.service.common.CommonMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements CommonMethod<UserDto, UserRequest> {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto save(UserRequest request) {
    if(userRepository.findByUsername(request.getUsername()).isPresent()
    || userRepository.findByEmail(request.getEmail()).isPresent()) throw new AlreadyJoinException("이미 가입되어 있습니다.");

    User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto update(UserRequest request) {
    if(userRepository.findByUsername(request.getUsername()).isEmpty()
    || userRepository.findByEmail(request.getEmail()).isEmpty()) throw new NoJoinUserException("해당 하는 계정이 없습니다.");

    User user = userRepository.findByEmail(request.getEmail()).get();
    user.setPassword(request.getPassword());
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Override
  public void delete(UserRequest request) {
    if(userRepository.findByUsername(request.getUsername()).isEmpty()
        || userRepository.findByEmail(request.getEmail()).isEmpty()) throw new NoJoinUserException("해당 하는 계정이 없습니다.");

    User user = userRepository.findByEmail(request.getEmail()).get();
    if(request.getPassword().equals(user.getPassword())) userRepository.delete(user);
    else throw new NoCorrectPasswordException("비밀번호가 맞지 않습니다.");
  }

  @Override
  public UserDto get(UserRequest request) {
    if(userRepository.findByUsername(request.getUsername()).isEmpty()
        || userRepository.findByEmail(request.getEmail()).isEmpty()) throw new NoJoinUserException("해당 하는 계정이 없습니다.");

    return userMapper.toDto(userRepository.findByEmail(request.getEmail()).get());
  }
}
