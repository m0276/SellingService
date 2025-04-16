package MJ.sellingservice.service;

import MJ.sellingservice.domain.Favorite;
import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.FavoriteDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.exception.custom.HaveToLoginException;
import MJ.sellingservice.mapper.FavoriteMapper;
import MJ.sellingservice.repository.FavoriteRepository;
import MJ.sellingservice.util.LoginUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;
  private final FavoriteMapper favoriteMapper;
  private final UserService userService;

  public List<FavoriteDto> get(){
    if(!LoginUtil.isLogin()) throw new HaveToLoginException("로그인 후 이용해 주세요.");

    String email = LoginUtil.getCurrentUserEmail();
    List<Favorite> favorites = favoriteRepository.findByUser_Email(email);

    List<FavoriteDto> result = new ArrayList<>();
    for(Favorite favorite: favorites){
      result.add(favoriteMapper.toDto(favorite));
    }

    return result;
  }

  public void checkAndSave(FavoriteDto favoriteDto) {
    if (!LoginUtil.isLogin()) throw new HaveToLoginException("로그인 후 이용해 주세요.");

    String email = LoginUtil.getCurrentUserEmail();
    User user = userService.findByUserEmail(email);

    try {
      if (favoriteDto.getFavorite()) {
        save(favoriteDto, user);
      } else {
        delete(favoriteDto, user);
      }
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("해당하는 즐겨찾기를 찾을 수 없습니다.");
    }
  }

  private void save(FavoriteDto favoriteDto, User user) {
    Favorite favorite = new Favorite(favoriteDto.getMarket_cd(), favoriteDto.getProduct_cd(),
        favoriteDto.getTrd_clcln_ymd(), favoriteDto.getScsbd_prc(), user);
    favoriteRepository.save(favorite);
  }

  private void delete(FavoriteDto favoriteDto, User user) {
    Favorite favorite = favoriteRepository.findByUserAndMarketCdAndProductCdAndTimeAndPrice(user,
            favoriteDto.getMarket_cd(), favoriteDto.getProduct_cd(), favoriteDto.getTrd_clcln_ymd(),
            favoriteDto.getScsbd_prc())
        .orElseThrow(() -> new NoSuchElementException("해당 즐겨찾기를 찾을 수 없습니다."));

    favoriteRepository.delete(favorite);
  }
}
