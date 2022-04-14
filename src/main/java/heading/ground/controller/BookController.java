package heading.ground.controller;

import heading.ground.dto.book.BookDto;
import heading.ground.entity.book.Book;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.repository.book.BookRepository;
import heading.ground.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookRepository bookRepository;
    private final BookService bookService;

    @GetMapping("/{id}")
    public String bookDetail(@PathVariable("id")Long id,Model model){
        Book books = bookRepository.findByIdWithCollections(id);
        BookDto book = new BookDto(books);
        model.addAttribute("book",book);

        return "book/bookDetail";
    }
    @PostMapping("/{id}/accept")
    public String bookAccept(@PathVariable("id") String id,
                             @SessionAttribute("user") BaseUser user){

        if(user instanceof Seller){
            Seller seller = (Seller)user;
            Optional<Book> bookWithSeller = bookRepository.findBookWithSeller(seller.getId());
            if(bookWithSeller.isEmpty()){
                //ㄴㄴ
                throw new IllegalStateException();
            }
        }
        Long l = Long.parseLong(id);
        log.info("id = {} ",id);
        bookService.process(l,true);
        return "redirect:/seller/account";
    }


    @GetMapping("/{id}/reject")
    public String bookReject(@PathVariable("id") Long id, Model model){
        //TODO 예약 내용 + 거절 사유 input
        Book book = bookRepository.findByIdWithCollections(id);
        BookDto bookDto = new BookDto(book);
        model.addAttribute("book",bookDto);

        return "book/bookReject";
    }


    @PostMapping("/{id}/reject")
    public String bookRejected(@PathVariable("id") Long id,
                               @RequestParam("reason") String reason){
        //TODO book에 거절 사유 update + 연관된 bookedMenus 삭제 + book.status = Canceled
        bookService.rejectBook(id,reason);

        return "redirect:/seller/account";
    }

}
