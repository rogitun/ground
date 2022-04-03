package heading.ground.repository.user;

import heading.ground.entity.user.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    long countByLoginId(@Param("loginId") String loginId);

    Optional<Seller> findByLoginId(String longId);

}
