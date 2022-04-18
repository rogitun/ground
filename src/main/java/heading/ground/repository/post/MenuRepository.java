package heading.ground.repository.post;

import heading.ground.entity.post.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    List<Menu> findBySellerId(Long sellerId);

    @Query("select m from Menu m " +
            "where m.seller.id in :id")
    List<Menu> selectMenuBySeller(@Param("id") Long id);

    Menu findByName(String name);

    @Query("select m from Menu m " +
            "join fetch m.seller s " +
            "where s.id = :sid")
    List<Menu> findMenusBySellerId(@Param("sid") Long id);

    @Query("select m from Menu m " +
            "join fetch m.seller s " +
            "where s.id = :sid and " +
            "m.id = :mid")
    Optional<Menu> findMenuByIdWithSeller(@Param("mid") Long id, @Param("sid") Long sid);
}
