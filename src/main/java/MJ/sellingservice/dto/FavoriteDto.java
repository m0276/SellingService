package MJ.sellingservice.dto;

import MJ.sellingservice.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteDto {
  Boolean favorite;
  String market_cd;
  String product_cd;
  String trd_clcln_ymd;
  String scsbd_prc;
  User user;
}
