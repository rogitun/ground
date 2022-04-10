package heading.ground.service;

import heading.ground.entity.book.Book;
import heading.ground.entity.book.BookedMenu;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.repository.book.BookRepository;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import heading.ground.repository.user.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final MenuRepository menuRepository;
    private final SellerRepository sellerRepository;


    //TODO 쿼리 최적화
    public List<BookedMenu> createBookMenus(HashMap<String, String> all) {
        List<BookedMenu> bookedMenus = new ArrayList<>();
        for(int i=1;i<=all.size()/2;i++){
            String name = all.get("menu"+i);
            int num = Integer.parseInt(all.get("quantity"+i));
            Menu menu = menuRepository.findByName(name);
            bookedMenus.add(new BookedMenu(menu, num));
        }
        return bookedMenus;
    }

    @Transactional
    public void createBook(List<BookedMenu> bookMenus, Long studentId, Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).get();
        Student student = studentRepository.findById(studentId).get();
        Book book = Book.book(seller, student, bookMenus);
        bookRepository.save(book);
    }

    public List<Book> findBooks(Long id) {
        List<Book> allBooks = bookRepository.findAllBooks(id);
        return allBooks;
    }
}
