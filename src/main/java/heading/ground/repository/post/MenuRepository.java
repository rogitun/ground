package heading.ground.repository.post;

import heading.ground.entity.post.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    List<Menu> findBySellerId(Long sellerId);

    @Query("select m.name from Menu m " +
            "where m.seller.id in :id")
    List<String> selectNameBySeller(@Param("id") Long id);

    Menu findByName(String name);

}
