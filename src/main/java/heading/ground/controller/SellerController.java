package heading.ground.controller;

import heading.ground.dto.AccountDto;
import heading.ground.entity.user.Address;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.LoginForm;
import heading.ground.forms.SellerEditForm;
import heading.ground.forms.SellerSignUpForm;
import heading.ground.repository.SellerRepository;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/account")     //가게 정보로 가는 메서드
    public String account(Model model, @SessionAttribute(name = "user",required = false) BaseUser seller){

        AccountDto accountDto = new AccountDto((Seller) seller);
        model.addAttribute("account",accountDto);
        model.addAttribute("accountId",((Seller) seller).getId());
        return "/user/account";
    }

    @GetMapping("/edit/{id}")  //정보 수정 메서드
    public String editAccount(@PathVariable("id") Long id,
                              @SessionAttribute(name = "user",required = false) BaseUser ses,
                              Model model){
        //TODO 세션으로 접근하는 사람과 수정 대상 데이터의 주인이 일치하는지 확인
        Seller seller = sellerRepository.findById(id).get();
        SellerEditForm sellerEditForm = new SellerEditForm(seller);

        model.addAttribute("seller",sellerEditForm);

        return "user/edit-account";
    }

    @PostMapping("/edit/{id}") //수정 POST
    public String edit(@Validated @ModelAttribute("seller") SellerEditForm form,BindingResult bindingResult,
                       @SessionAttribute(name = "user",required = false) BaseUser ses,
                       HttpServletRequest request){

        if(bindingResult.hasErrors()){
            return "user/edit-account";
        }
        Seller seller = (Seller) ses; //변경 감지로 데이터 변경
        Seller updatedSeller = userService.updateSeller(seller.getId(), form);
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user",updatedSeller);

        return "redirect:/seller/account";
    }

}
