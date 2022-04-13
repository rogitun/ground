package heading.ground.repository.book;

import heading.ground.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("select b from Book b where :id in (b.seller.id, b.student.id)")
    List<Book> findAllBooks(@Param("id") Long id);

    //TODO Book - bookedmenu - menu fetccJoin 처리

}
