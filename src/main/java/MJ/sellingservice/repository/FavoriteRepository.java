package MJ.sellingservice.repository;


import MJ.sellingservice.domain.Favorite;
import MJ.sellingservice.domain.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  Optional<Favorite> findByUserAndMarketCdAndProductCdAndTimeAndPrice(User user, String marketCd, String productCd, String time,
      String price);

  List<Favorite> findByUser_Email(String userEmail);
}
