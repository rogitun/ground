package heading.ground.controller;

import heading.ground.dto.book.BookDto;
import heading.ground.dto.book.MenuListDto;
import heading.ground.dto.post.MenuDto;
import heading.ground.dto.user.SellerDto;
import heading.ground.entity.book.Book;
import heading.ground.entity.book.BookedMenu;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.book.BookForm;
import heading.ground.forms.user.LoginForm;
import heading.ground.forms.user.SellerEditForm;
import heading.ground.forms.user.SellerSignUpForm;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import heading.ground.repository.user.UserRepository;
import heading.ground.service.BookService;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/edit/{id}")  //정보 수정 메서드
    public String editAccount(@PathVariable("id") Long id,
                              @SessionAttribute(name = "user", required = false) BaseUser ses,
                              Model model) {
        //TODO 세션으로 접근하는 사람과 수정 대상 데이터의 주인이 일치하는지 확인
        Seller seller = sellerRepository.findById(id).get();
        SellerEditForm sellerEditForm = new SellerEditForm(seller);

        model.addAttribute("seller", sellerEditForm);

        return "user/edit-account";
    }

    @PostMapping("/edit/{id}") //수정 POST
    public String edit(@Validated @ModelAttribute("seller") SellerEditForm form, BindingResult bindingResult,
                       @SessionAttribute(name = "user", required = false) BaseUser ses,
                       HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            return "user/edit-account";
        }
        //파일처리 TODO 파일 이름 UUID로 수정
        //TODO 사진이 다르면 기존의 사진 delete

        //가게 데이터 변경 처리 + 파일처리
        Seller seller = (Seller) ses; //변경 감지로 데이터 변경
        Seller updatedSeller = userService.updateSeller(seller.getId(), form);

        log.info("controller_ seller = {}", seller.getImageFile());

        //세션 업데이트
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user", updatedSeller);

        return "redirect:/seller/account";
    }

    @GetMapping("/{id}")
    public String sellerInfo(@PathVariable("id") Long id, Model model) {
        //Seller seller = sellerRepository.findById(id).get();
        //TODO Seller + Menu + Comment
        Seller seller = userRepository.findByIdWithMenuComment(id);
        SellerDto sellerDto = new SellerDto();
        sellerDto.setSellerWithMenus(seller);
        //SellerDto sellerDto = new SellerDto(seller);
        List<MenuDto> menus = sellerDto.getMenus();
        List<MenuDto> best = menus.stream().filter(m -> m.isBest()).collect(Collectors.toList());

        model.addAttribute("seller", sellerDto);
        model.addAttribute("menus", menus);
        model.addAttribute("best", best);
        //TODO 메뉴도 같이 보여줘야 합니다.

        return "user/seller";
    }

    @GetMapping("/{id}/bookRequest") //예약 폼
    public void bookRequest(@PathVariable("id") Long id,
                           HttpServletResponse response) throws IOException {
        long l = bookService.findStock(id);
        log.info("size =  {}",l);
        if (l <= 0) {
            response.sendError(500,"데이터 없음");
        }
    }

    @GetMapping("/{id}/book") //예약 폼
    public String bookForm(@PathVariable("id") Long id, Model model,
                           @SessionAttribute("user") BaseUser std,
                           HttpServletResponse response) throws IOException {
        if (!(std instanceof Student)) {
            return "redirect:/seller/" + id;
        }

        List<Menu> menuList = menuRepository.selectMenuBySeller(id);
        List<MenuListDto> menus = menuList.stream().map(m -> new MenuListDto(m)).collect(Collectors.toList());
        model.addAttribute("form", menus);
        model.addAttribute("sellerId", id);
        return "/book/bookForm";
    }

    @PostMapping("/{id}/book")
    public String addBook(@PathVariable("id") Long id, @RequestBody BookForm form,
                          HttpServletResponse response,
                          @SessionAttribute("user") BaseUser std) throws IOException {

        //TODO 메뉴 품절 체크
        List<BookedMenu> bookMenus = bookService.createBookMenus(form.returnArr());
        if(bookMenus == null)
            response.sendError(500,"메뉴 오류");

        Student student = (Student) std;
        bookService.createBook(bookMenus, student.getId(), id, form);

        return "redirect:/seller/" + id;
    }
}
