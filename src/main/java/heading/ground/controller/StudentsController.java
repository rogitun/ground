package heading.ground.controller;

import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Student;
import heading.ground.forms.user.StudentForm;
import heading.ground.forms.user.LoginForm;
import heading.ground.repository.user.StudentRepository;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StudentsController {

    private final StudentRepository studentRepository;
    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("user") LoginForm studentLoginForm){
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("user") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request){
        if(bindingResult.hasErrors()){
            log.info("field Error");
            return "user/login";
        }
        //글로벌 에러처리는 아이디 혹은 비밀번호 존재하지 않음
        BaseUser user = userService.logIn(form.getLoginId(), form.getPassword());
        if(user!=null){
            log.info("pass");
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            return "redirect:/"; //홈화면 이동
        }

        log.info("failed");
        bindingResult.reject("NotFound","아이디 혹은 비밀번호 불일치");
        return "user/login";
    }

    @PostMapping("/logout")
    public String logOut(HttpServletRequest request){
        HttpSession session = request.getSession(false); //세션이 있으면 세션 반환
        if(session!=null)
            session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/signup")
    public String signUpForm(Model model){
        model.addAttribute("user",new StudentForm());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Validated @ModelAttribute(name = "user") StudentForm studentForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        //비밀번호는 글로벌 에러 처리함.
        if(!studentForm.getPassword().equals(studentForm.getPassword2())){ //비밀번호 불일치
            bindingResult.reject("password","비밀번호가 일치하지 않습니다.");
        }
        Optional<Student> byLoginId = studentRepository.findByLoginId(studentForm.getLoginId());
        if(byLoginId.isPresent()){
            bindingResult.reject("loginFail","이미 사용중인 아이디입니다.");
            return "user/signup";
        }

        if(bindingResult.hasErrors()){
            log.info(bindingResult.toString());
            System.out.println(" = ");
            return "user/signup";
        }

        Student student = studentForm.toEntity();
        studentRepository.save(student);
        return "redirect:/user/login";
    }

}