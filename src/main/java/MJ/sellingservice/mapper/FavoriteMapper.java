package MJ.sellingservice.mapper;

import MJ.sellingservice.domain.Favorite;
import MJ.sellingservice.domain.User;
import MJ.sellingservice.dto.FavoriteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

  @Mapping(target = "market_cd",source = "marketCd")
  @Mapping(target = "product_cd", source = "productCd")
  @Mapping(target = "trd_clcln_ymd", source = "time")
  @Mapping(target = "scsbd_prc", source = "price")
  FavoriteDto toDto(Favorite favorite);
}