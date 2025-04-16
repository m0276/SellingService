package MJ.sellingservice.service;

import MJ.sellingservice.domain.Favorite;
import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.FavoriteDto;
import MJ.sellingservice.dto.request.UserRequest;
import MJ.sellingservice.repository.FavoriteRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;

  public boolean checkAndSave(FavoriteDto favoriteDto, User user){
    try {
      if(favoriteDto.getFavorite()) save(favoriteDto.getMarket_cd(), favoriteDto.getProduct_cd(),
          favoriteDto.getTrd_clcln_ymd(), favoriteDto.getScsbd_prc(), user);
      else delete(favoriteDto.getMarket_cd(), favoriteDto.getProduct_cd(),
          favoriteDto.getTrd_clcln_ymd(), favoriteDto.getScsbd_prc(), user);
      return true;
    } catch (NoSuchElementException e){
      return false;
    }
  }

  private void save(String market_cd,String product_cd,String time,String price, User user){
    Favorite favorite = new Favorite(market_cd,product_cd,time,price,user);
    favoriteRepository.save(favorite);
  }

  private void delete(String market_cd,String product_cd,String time,String price, User user){
    if(favoriteRepository.findByUserAndMarketCdAndProductCdAndTimeAndPrice(user,market_cd,product_cd,time,price).isEmpty()){
      throw new NoSuchElementException();
    }

    Favorite favorite = favoriteRepository.findByUserAndMarketCdAndProductCdAndTimeAndPrice(user,market_cd,product_cd,time,price).get();

    favoriteRepository.delete(favorite);
  }
}
