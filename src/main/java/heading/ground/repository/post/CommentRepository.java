package heading.ground.repository.post;

import heading.ground.entity.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c where c.menu.id = :id")
    List<Comment> findByMenuId(@Param("id") Long id);
}
