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
  String market_cd;
  String product_cd;
  String trd_clcln_ymd;
  String scsbd_prc;
  User user;

  @Override
  public String toString() {
    return "FavoriteDto{" +
        "market_cd='" + market_cd + '\'' +
        ", product_cd='" + product_cd + '\'' +
        ", trd_clcln_ymd='" + trd_clcln_ymd + '\'' +
        ", scsbd_prc='" + scsbd_prc + '\'' +
        ", user=" + user.getUsername() +
        '}';
  }
}
