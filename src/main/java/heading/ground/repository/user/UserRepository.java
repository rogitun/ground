package heading.ground.repository.user;

import heading.ground.entity.user.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<BaseUser,Long> {


    @Query("select u.name from BaseUser u " +
            "where u.id = :uid")
    String findSellerById(@Param("uid") Long id);
}
