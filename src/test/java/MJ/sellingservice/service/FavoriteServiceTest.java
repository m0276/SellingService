package MJ.sellingservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import MJ.sellingservice.domain.Favorite;
import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.FavoriteDto;
import MJ.sellingservice.mapper.UserMapper;
import MJ.sellingservice.repository.FavoriteRepository;
import MJ.sellingservice.util.LoginUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
  @Mock
  private FavoriteRepository favoriteRepository;

  @Mock
  private UserService userService;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private FavoriteService favoriteService;



  @Test
  void get() {

    try (MockedStatic<LoginUtil> mockedLoginUtil = mockStatic(LoginUtil.class)) {
      //when(favoriteRepository.findAll()).thenReturn(Collections.emptyList());
      mockedLoginUtil.when(LoginUtil::isLogin).thenReturn(true);
      mockedLoginUtil.when(LoginUtil::getCurrentUserEmail).thenReturn("test@example.com");

      List<FavoriteDto> list =  favoriteService.get();

      assertTrue(list.isEmpty(), "empty List");
    }
  }

  @Test
  void checkAndSave_shouldSave() {
    try (MockedStatic<LoginUtil> mockedLoginUtil = mockStatic(LoginUtil.class)) {
      mockedLoginUtil.when(LoginUtil::isLogin).thenReturn(true);
      mockedLoginUtil.when(LoginUtil::getCurrentUserEmail).thenReturn("test@example.com");

      User mockUser = mock(User.class);
      when(userService.findByUserEmail("test@example.com")).thenReturn(mockUser);

      FavoriteDto favoriteDto = new FavoriteDto();
      favoriteDto.setFavorite(true);
      favoriteService.checkAndSave(favoriteDto);

      verify(favoriteRepository).save(any(Favorite.class));
      verify(favoriteRepository, never()).delete(any(Favorite.class));
    }
  }

  @Test
  void checkAndSave_shouldDelete() {
    try (MockedStatic<LoginUtil> mockedLoginUtil = mockStatic(LoginUtil.class)) {
      mockedLoginUtil.when(LoginUtil::isLogin).thenReturn(true);
      mockedLoginUtil.when(LoginUtil::getCurrentUserEmail).thenReturn("test@example.com");

      User mockUser = mock(User.class);
      when(userService.findByUserEmail("test@example.com")).thenReturn(mockUser);

      FavoriteDto favoriteDto = new FavoriteDto();
      favoriteDto.setFavorite(false);

      assertThrows(NoSuchElementException.class,() -> favoriteService.checkAndSave(favoriteDto),"해당하는 즐겨찾기를 찾을 수 없습니다.");
    }
  }

}