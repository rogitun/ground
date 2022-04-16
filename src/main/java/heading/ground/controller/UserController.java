package heading.ground.controller;

import heading.ground.dto.book.BookDto;
import heading.ground.dto.post.MenuDto;
import heading.ground.dto.user.SellerDto;
import heading.ground.dto.user.StudentDto;
import heading.ground.entity.book.Book;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.user.BaseSignUp;
import heading.ground.forms.user.LoginForm;
import heading.ground.repository.user.UserRepository;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") LoginForm form) {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("user", new BaseSignUp());
        return "user/signup";
    }

    @GetMapping("/signup-seller")
    public String sellerSignUpForm(Model model) {
        model.addAttribute("user", new BaseSignUp());
        return "/user/seller-signup";
    }

    @PostMapping("/signup")
    public String signUp(@Validated @ModelAttribute("user") BaseSignUp form,
                         BindingResult bindingResult,
                         @RequestParam(value = "companyId",required = false) String cId) {
        log.info("cid = {} ",cId);

        if (bindingResult.hasErrors()) { //필드 에러 처리
            return "/user/seller-signup";
        }

        if (!form.getPassword().equals(form.getPassword2())) {//비밀번호 다름(글로벌 에러 처리)
            bindingResult.reject("NotEquals", "비밀번호가 서로 일치하지 않습니다.");
            return "/user/seller-signup";
        }

        //TODO 서비스 처리 / 이미 가입된 아이디인지 체크
        long is_duplicated = userRepository.countByLoginId(form.getLoginId());
        if (is_duplicated > 0) {//중복된 아이디임;
            bindingResult.reject("Duplicate", "이미 가입된 아이디");
            return "/user/seller-signup";
        }
        if(cId != null){
            userRepository.save(form.toSeller(form));
        }
        else{
            userRepository.save(form.toStudent(form));
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("user") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            log.info("field Error");
            return "user/login";
        }
        //글로벌 에러처리는 아이디 혹은 비밀번호 존재하지 않음
        BaseUser user = userService.logIn(form.getLoginId(), form.getPassword());
        if (user != null) {
            log.info("pass");
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "redirect:/"; //홈화면 이동
        }

        log.info("failed");
        bindingResult.reject("NotFound", "아이디 혹은 비밀번호 불일치");
        return "user/login";
    }

    @PostMapping("/logout")
    public String logOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false); //세션이 있으면 세션 반환
        if (session != null)
            session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/profile")     //가게 정보로 가는 메서드
    public String profile(Model model,
                          @SessionAttribute(name = "user") BaseUser user) {

        if(user instanceof Seller) {
            log.info("seller");
            //TODO Seller & Menu 한꺼번에 fetch Join으로 가져와서 DTO 전환
            Seller session = (Seller) user;
            Seller seller = userRepository.findSellerByIdForAccount(session.getId());
            log.info("query track 1 {}", seller);
            SellerDto sellerDto = new SellerDto(seller);
            log.info("query track2");
            List<MenuDto> menuDto = sellerDto.getMenus();
            log.info("query track3");
            List<BookDto> bookDto = sellerDto.getBooks();

            model.addAttribute("books", bookDto);
            model.addAttribute("menus", menuDto);
            model.addAttribute("account", sellerDto);


            return "/user/account";
        }
        if(user instanceof Student){
            Student session = (Student) user;
            Student student = userRepository.findStudentByIdForAccount(session.getId());
            StudentDto studentDto = new StudentDto(student.getName(), student.getEmail());

            //TODO Books service에서 처리하기
            log.info("student_id = {}",student.getId());
            List<Book> booksForStudent = student.getBooks();
            List<BookDto> bookDtos = booksForStudent.stream()
                    .map(b -> new BookDto(b))
                    .collect(Collectors.toList());

            model.addAttribute("student", studentDto);
            model.addAttribute("books", bookDtos);

            return "user/student";
        }
        return "redirect:/";
    }

    //TODO 유저의 정보 수정 또한 동일하다

}
