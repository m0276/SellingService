package MJ.sellingservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite")
@Getter
@NoArgsConstructor
public class Favorite {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "market_cd")
  String marketCd;

  @Column(name = "product_cd")
  String productCd;

  @Column
  String time;

  @Column
  String price;

  @ManyToOne @JoinColumn(name = "user_id")
  User user;

  public Favorite(String market_cd, String product_cd, String time, String price, User user) {
    this.marketCd = market_cd;
    this.productCd = product_cd;
    this.time = time;
    this.price = price;
    this.user = user;
  }
}
