package heading.ground.dto.book;

import heading.ground.dto.user.StudentDto;
import heading.ground.entity.book.Book;
import heading.ground.entity.book.BookStatus;
import heading.ground.entity.book.BookType;
import heading.ground.entity.book.Payment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookDto {

    private BookStatus status;

    private BookType type; //방문 타입

    private Payment payment; //결제 타입

    private int totalPrice;

    private LocalDateTime bookTime;

    private int number; //사람 몇명 오는지

    private StudentDto student; //예약자

    private BookSellerDto seller;

    private List<BookedMenuDto> bookedMenus = new ArrayList<>(); //예약된 메뉴들

    public BookDto(Book book) {
        status = book.getStatus();
        type = book.getType();
        payment = book.getPayment();
        totalPrice = book.getTotalPrice();
        bookTime = book.getBookTime();
        number = book.getNumber();
        student = new StudentDto(book.getStudent().getName());
        seller = new BookSellerDto(book.getSeller());
        book.getBookedMenus()
                .stream()
                .map(bm -> new BookedMenuDto(bm))
                .forEach(s -> bookedMenus.add(s));
    }
}
