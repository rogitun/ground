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
import heading.ground.forms.user.SellerEditForm;
import heading.ground.forms.user.UserEditForm;
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
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") LoginForm form,
                      HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session!=null) return "redirect:/";
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
                         @RequestParam(value = "companyId", required = false) String cId) {
        log.info("cid = {} ", cId);

        if (bindingResult.hasErrors()) { //?????? ?????? ??????
            return "/user/seller-signup";
        }

        if (!form.getPassword().equals(form.getPassword2())) {//???????????? ??????(????????? ?????? ??????)
            bindingResult.reject("NotEquals", "??????????????? ?????? ???????????? ????????????.");
            return "/user/seller-signup";
        }

        //TODO ????????? ?????? / ?????? ????????? ??????????????? ??????
        long is_duplicated = userRepository.countByLoginId(form.getLoginId());
        if (is_duplicated > 0) {//????????? ????????????;
            bindingResult.reject("Duplicate", "?????? ????????? ?????????");
            return "/user/seller-signup";
        }
        if (cId != null) {
            userRepository.save(form.toSeller(form));
        } else {
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
        //????????? ??????????????? ????????? ?????? ???????????? ???????????? ??????
        BaseUser user = userService.logIn(form.getLoginId(), form.getPassword());
        if (user != null) {
            log.info("pass");
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "redirect:/"; //????????? ??????
        }

        log.info("failed");
        bindingResult.reject("NotFound", "????????? ?????? ???????????? ?????????");
        return "user/login";
    }

    @PostMapping("/logout")
    public String logOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false); //????????? ????????? ?????? ??????
        if (session != null)
            session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/profile")     //?????? ????????? ?????? ?????????
    public String profile(Model model,
                          @SessionAttribute(name = "user") BaseUser user) {

        if (user instanceof Seller) {
            log.info("seller");
            //TODO Seller & Menu ???????????? fetch Join?????? ???????????? DTO ??????
            Seller seller = userRepository.findSellerByIdForAccount(user.getId());
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
        if (user instanceof Student) {
            Student student = userRepository.findStudentByIdForAccount(user.getId());
            StudentDto studentDto = new StudentDto(student.getId(),student.getName(), student.getEmail());

            //TODO Books service?????? ????????????
            log.info("student_id = {}", student.getId());
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

    //TODO ????????? ?????? ?????? ?????? ????????????
    @GetMapping("/edit/{id}")  //?????? ?????? ?????????
    public String editAccount(@PathVariable("id") Long id,
                              @SessionAttribute(name = "user", required = false) BaseUser ses,
                              Model model) {
        //TODO ???????????? ???????????? ????????? ?????? ?????? ???????????? ????????? ??????????????? ??????
        //TODO ?????? ????????? ???????????? ???????????? ??????
        if (ses instanceof Seller && ses.getId() == id) {
            Seller seller = (Seller) userRepository.findById(id).get();
            SellerEditForm sellerEditForm = new SellerEditForm(seller);
            model.addAttribute("seller", sellerEditForm);
            return "user/edit-seller";
        } else if (ses instanceof Student && ses.getId() == id) {
            UserEditForm user = new UserEditForm((Student) ses);
            model.addAttribute("user",user);
            return "user/edit-student";
        }
        return "redirect:/";
    }

    @PostMapping("/edit/{id}") //?????? POST
    public String editSeller(@Validated @ModelAttribute("seller") SellerEditForm form, BindingResult bindingResult,
                             @SessionAttribute(name = "user") BaseUser ses,
                             @PathVariable("id") Long id,
                             HttpServletRequest request) throws IOException {
        log.info("tracking 1");
        if (bindingResult.hasErrors() || id != ses.getId()) {
            log.info("bionding = {}",bindingResult);
            return "user/edit-seller";
        }
        log.info("edit tracking 1 ");
        //?????? ????????? ?????? ?????? + ????????????
        Seller updatedSeller = userService.updateSeller(ses.getId(), form);
        log.info("edit tracking 2 ");
        //?????? ????????????
        HttpSession session = request.getSession(false);
        log.info("edit tracking 3 ");
        session.setAttribute("user", updatedSeller);
        log.info("edit tracking 4 ");
        return "redirect:/profile";
    }

   @PostMapping("/edit-student/{id}") //?????? POST
    public String editStudent(@Validated @ModelAttribute("user") UserEditForm form, BindingResult bindingResult,
                             @SessionAttribute(name = "user") BaseUser ses,
                             @PathVariable("id") Long id,
                             HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors() || id != ses.getId()) {
            return "user/edit-student";
        }
       Student student = userService.updateStudent(id, form);
       HttpSession session = request.getSession();
        session.setAttribute("user",student);

        return "redirect:/profile";
    }

}
