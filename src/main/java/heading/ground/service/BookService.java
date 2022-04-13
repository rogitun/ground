package heading.ground.service;

import heading.ground.entity.book.Book;
import heading.ground.entity.book.BookedMenu;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.book.BookForm;
import heading.ground.forms.book.MenuSet;
import heading.ground.repository.book.BookRepository;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import heading.ground.repository.user.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final MenuRepository menuRepository;
    private final SellerRepository sellerRepository;


    //TODO 쿼리 최적화
    public List<BookedMenu> createBookMenus(List<MenuSet> form) {
        List<BookedMenu> bookedMenus = new ArrayList<>();
        for (MenuSet menuSet : form) {
            Menu byName = menuRepository.findByName(menuSet.getName());
            bookedMenus.add(new BookedMenu(byName,menuSet.getQuantity()));
        }
        return bookedMenus;
    }

    @Transactional
    public void createBook(List<BookedMenu> bookMenus, Long studentId, Long sellerId,BookForm form) {
        Seller seller = sellerRepository.findById(sellerId).get();
        Student student = studentRepository.findById(studentId).get();
        Book book = Book.book(seller, student, bookMenus,form);
        bookRepository.save(book);
    }

    public List<Book> findBooksForSeller(Long id) {
        List<Book> allBooks = bookRepository.findAllBooksForSeller(id);
        return allBooks;
    }

    public List<Book> findBooksForStudent(Long id) {
        List<Book> allBooks = bookRepository.findAllBooksForStudent(id);
        return allBooks;
    }

    @Transactional
    public void process(Long id,boolean flag) {
        Book book = bookRepository.findById(id).get();
        book.processBook(flag);
    }
}
