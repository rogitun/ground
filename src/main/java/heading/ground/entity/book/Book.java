package heading.ground.entity.book;

import heading.ground.entity.Base;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.book.BookForm;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Book extends Base {

    @Id @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Enumerated(EnumType.STRING)
    private BookType type; //방문 타입

    @Enumerated(EnumType.STRING)
    private Payment payment; //결제 타입

    private int totalPrice;

    private LocalDateTime bookTime;

    private int number; //사람 몇명 오는지

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student; //예약자

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL)
    private List<BookedMenu> bookedMenus= new ArrayList<>(); //예약된 메뉴들


    public static Book book(Seller seller, Student student, List<BookedMenu> bookedMenus,
                            BookForm form){
        Book book = new Book();
        book.setBook(seller,student,bookedMenus);
        book.setField(form);
        return book;
    }

    private void setField(BookForm form) {
        if(form.getType().equals("togo")){
            this.type = BookType.TOGO;
        }
        else type = BookType.HERE;

        if(form.getPayment().equals("cash"))
            payment = Payment.CASH;
        else payment = Payment.CREDIT;

        number = form.getNumber();
    }

    public void setBook(Seller seller,Student student,List<BookedMenu> bookedMenus) {
        for (BookedMenu bookedMenu : bookedMenus) {
            this.bookedMenus.add(bookedMenu);
            this.totalPrice += (bookedMenu.getPrice() * bookedMenu.getQuantity());
        }
        this.seller = seller;
        this.student = student;

        student.getBooks().add(this);
        seller.getBooks().add(this);

        this.status = BookStatus.PENDING;
        this.bookTime = LocalDateTime.now();
    }

}
