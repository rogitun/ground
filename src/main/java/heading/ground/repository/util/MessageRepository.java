package heading.ground.repository.util;


import heading.ground.entity.util.Message;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select m from Message m " +
            "join fetch m.writer w " +
            "join fetch m.receiver r " +
            "where :uid in (r.id, w.id)")
    List<Message> findAllMessagesById(@Param("uid") Long id);

    @EntityGraph(attributePaths = {"writer","receiver"})
    Optional<Message> findGraphById(Long id);


}
