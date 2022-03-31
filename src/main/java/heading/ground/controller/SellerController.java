package heading.ground.controller;

import heading.ground.entity.user.Address;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.LoginForm;
import heading.ground.forms.SellerSignUpForm;
import heading.ground.repository.SellerRepository;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerRepository sellerRepository;
    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("user",new LoginForm());
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model){
        model.addAttribute("seller",new SellerSignUpForm());
        return "/user/seller-signup";
    }

    @PostMapping("/signup")
    public String signUp(@Validated @ModelAttribute("seller") SellerSignUpForm form,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){ //필드 에러 처리
            return "/user/seller-signup";
        }

        if(!form.getPassword().equals(form.getPassword2())){//비밀번호 다름(글로벌 에러 처리)
            bindingResult.reject("NotEquals","비밀번호가 서로 일치하지 않습니다.");
            return "/user/seller-signup";
        }
        
        //이미 가입된 아이디인지 체크
        long is_duplicated = sellerRepository.countByLoginId(form.getLoginId());
        if(is_duplicated>0){//중복된 아이디임;
            bindingResult.reject("Duplicate","이미 가입된 아이디");
            return "/user/seller-signup";
        }

        Seller seller = form.toEntity();
        sellerRepository.save(seller);

        return "redirect:/login";
    }

}
