package heading.ground.controller;

import heading.ground.dto.SellerDto;
import heading.ground.entity.ImageFile;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.file.FileStore;
import heading.ground.file.FileUploadUtil;
import heading.ground.file.fileRepository;
import heading.ground.forms.post.MenuForm;
import heading.ground.forms.user.LoginForm;
import heading.ground.forms.user.SellerEditForm;
import heading.ground.forms.user.SellerSignUpForm;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import heading.ground.service.PostService;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerRepository sellerRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final PostService postService;

    private final FileStore fileStore;
    private final fileRepository fileRepository;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new LoginForm());
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("seller", new SellerSignUpForm());
        return "/user/seller-signup";
    }

    @PostMapping("/signup")
    public String signUp(@Validated @ModelAttribute("seller") SellerSignUpForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //필드 에러 처리
            return "/user/seller-signup";
        }

        if (!form.getPassword().equals(form.getPassword2())) {//비밀번호 다름(글로벌 에러 처리)
            bindingResult.reject("NotEquals", "비밀번호가 서로 일치하지 않습니다.");
            return "/user/seller-signup";
        }

        //이미 가입된 아이디인지 체크
        long is_duplicated = sellerRepository.countByLoginId(form.getLoginId());
        if (is_duplicated > 0) {//중복된 아이디임;
            bindingResult.reject("Duplicate", "이미 가입된 아이디");
            return "/user/seller-signup";
        }

        Seller seller = form.toEntity();
        sellerRepository.save(seller);

        return "redirect:/login";
    }

    @GetMapping("/account")     //가게 정보로 가는 메서드
    public String account(Model model, @SessionAttribute(name = "user", required = false) BaseUser sessionSeller) {

        Seller seller = (Seller) sessionSeller;
        Seller sellerEntity = sellerRepository.findById(seller.getId()).get();
        SellerDto sellerDto = new SellerDto(sellerEntity);

        List<Menu> menus = menuRepository.findBySellerId(seller.getId());

        model.addAttribute("menus", menus);
        model.addAttribute("account", sellerDto);
        //model.addAttribute("accountId", seller.getId());
        return "/user/account";
    }

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

        MultipartFile multipartFile = form.getImageFile();
        //String fileName = toUUID(multipartFile);
        String fileName = multipartFile.getOriginalFilename();


        //가게 데이터 변경 처리
        Seller seller = (Seller) ses; //변경 감지로 데이터 변경
        Seller updatedSeller = userService.updateSeller(seller.getId(), form);

        //파일 저장소에 저장
        String uploadDir = "src/main/resources/static/files/" + updatedSeller.getId();
        FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        //세션 업데이트
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("user", updatedSeller);

        return "redirect:/seller/account";
    }

    @GetMapping("/add-menu")
    public String menuForm(Model model) { //메뉴 폼
        model.addAttribute("menu", new MenuForm());
        return "post/menuForm";
    }

    @PostMapping("/add-menu")
    public String addMenu(@Validated @ModelAttribute("menu") MenuForm form, BindingResult bindingResult,
                          @SessionAttribute(name = "user", required = false) BaseUser seller) { //메뉴 폼
        if (bindingResult.hasErrors()) {
            return "post/menuForm";
        }
        Menu menu = form.toEntity();
        Seller se = (Seller) seller;
        Menu save = menuRepository.save(menu);
        postService.addMenu(save, se);

        return "redirect:/seller/account";
    }

    private String toUUID(MultipartFile multipartFile) {
        int pos = multipartFile.getOriginalFilename().lastIndexOf(".");//확장자까지 찾기
        String ext = multipartFile.getOriginalFilename().substring(pos); //확장자
        String fileName = UUID.randomUUID().toString();
        return fileName + ext;
    }


}
