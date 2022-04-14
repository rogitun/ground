package heading.ground.repository.user;

import heading.ground.entity.user.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    long countByLoginId(@Param("loginId") String loginId);

    Optional<Seller> findByLoginId(String longId);

//    //TODO Seller & Menu 최적화
    @Query(value = "select distinct s from Seller s " +
            "join fetch s.menus m",
            countQuery = "select count(s) from Seller s")
    Page<Seller> findAll(PageRequest pageRequest);

    @Query("select s from Seller s " +
            "join fetch s.menus m " +
            "where s.id =:id")
    Seller findByIdWithMenu(@Param("id") Long id);



}
