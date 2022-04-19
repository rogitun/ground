package heading.ground.controller;

import heading.ground.dto.post.CommentDto;
import heading.ground.dto.post.MenuDto;
import heading.ground.dto.Paging;
import heading.ground.dto.post.MenuManageDto;
import heading.ground.entity.post.Comment;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.post.CommentForm;
import heading.ground.forms.post.MenuForm;
import heading.ground.repository.post.CommentRepository;
import heading.ground.repository.post.MenuRepository;
import heading.ground.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.io.IOException;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//메뉴, 댓글, 리뷰 등을 관리합니다.

/**
 * 메뉴는 /menus/id -> 사진 1개
 * 리뷰는 /menus/id/review -> 사진 다량
 * 댓글은 /menus/id/comment -> 사진 없음
 */
@Slf4j
@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
public class PostController {
    private final MenuRepository menuRepository;
    private final PostService postService;
    private final CommentRepository commentRepository;


    @GetMapping
    public String menuList(Model model, Pageable pageable) {
        int page = (pageable.getPageNumber()==0)? 0: (pageable.getPageNumber()-1);
        Page<MenuDto> all = postService.page(page, 9);//현재 인덱스, 보여줄 객체 갯수
        Paging paging = postService.pageTemp(all);

        model.addAttribute("menus", all);
        model.addAttribute("paging",paging);
       // model.addAttribute("paging",paging);
        return "post/menus";
    }

    @GetMapping("/add-menu")
    public String menuForm(Model model,
                           @SessionAttribute("user") BaseUser user) { //메뉴 폼
        if(!(user instanceof Seller))
            return "redirect:/";
        model.addAttribute("menu", new MenuForm());
        return "post/menuForm";
    }

    @PostMapping("/add-menu")
    public String addMenu(@Validated @ModelAttribute("menu") MenuForm form, BindingResult bindingResult,
                          @SessionAttribute(name = "user") BaseUser seller) throws IOException { //메뉴 폼
        if (bindingResult.hasErrors()) {
            return "post/menuForm";
        }
        Seller se = (Seller) seller;
        postService.addMenu(form, se);

        return "redirect:/profile";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model,
                           @SessionAttribute("user") BaseUser user) {
        Optional<Menu> optionalMenu = menuRepository.findMenuByIdWithSeller(id, user.getId());
        //Menu menu = menuRepository.findById(id).get();
        if(optionalMenu.isEmpty())
            return "redirect:/";
        Menu menu = optionalMenu.get();
        MenuForm form = new MenuForm(menu);
        model.addAttribute("menu", form);

        return "post/menuForm";
    }

    @PostMapping("/{id}/edit")
    public String editMenu(@Validated @ModelAttribute("menu") MenuForm form, BindingResult bindingResult,
                           @PathVariable("id") Long id,
                           @SessionAttribute("user") BaseUser user) throws IOException {
        Optional<Menu> optionalMenu = menuRepository.findMenuByIdWithSeller(id, user.getId());
        if(optionalMenu.isEmpty())
            return "redirect:/";
        Menu menu = optionalMenu.get();
       // Menu menu = menuRepository.findById(id).get();
        postService.updateMenu(menu, form);
        return "redirect:/profile";
    }

    @GetMapping("/{id}")
    public String singleMenu(@PathVariable("id") Long id, Model model,
                             @SessionAttribute(value = "user",required = false) BaseUser user) {

        CommentForm commentForm = new CommentForm();
        if (user instanceof Student)
            commentForm.setStudent(true);

        Optional<Menu> optional = menuRepository.findMenuByIdWithCoSe(id);
        if(optional.isEmpty())
            return "redirect:/";
        Menu entity = optional.get();
        MenuDto menu = new MenuDto(entity);

        //댓글 목록 가져오기
        List<Comment> comments = entity.getComments();
        if (comments !=null) {
            List<CommentDto> commentDtos = comments.stream()
                    .map(c -> new CommentDto(c))
                    .collect(Collectors.toList());
            model.addAttribute("comments",commentDtos);
        }

        model.addAttribute("menu", menu);
        model.addAttribute("sellerId", entity.getSeller().getId());
        model.addAttribute("comment", commentForm);

        log.info("menu-comment-size-on-page = {}",entity.getComments().size());
        return "post/menu";
    }

    @GetMapping("/manage")
    public String managingMenus(Model model,
                                @SessionAttribute("user") BaseUser user){
        if(!(user instanceof Seller))
            return "redirect:/";
        List<Menu> menus = menuRepository.findMenusBySellerId(user.getId());
        List<MenuManageDto> menuDtos = menus.stream().map(m -> new MenuManageDto(m)).collect(Collectors.toList());
        model.addAttribute("menus",menuDtos);
        return "/post/manage-menu";
    }

    @PostMapping("/{id}/status")
    public String setStatus(@PathVariable("id") Long id,
                            @RequestBody Map<String,String> data){

        log.info("controller In");
        String flag = data.get("data");
        postService.setMenuStatus(id,flag);
        return "redirect:/menus/manage";
    }

    @PostMapping("/{id}/del")
    public String delMenu(@PathVariable("id") Long id,
                          @SessionAttribute("user") BaseUser user){
        Optional<Menu> optionalMenu = menuRepository.findMenuByIdWithSeller(id, user.getId());
        if(optionalMenu.isEmpty()){
            return null;
        }
        menuRepository.deleteById(id);
        return "redirect:/menus/manage";
    }


    //TODO 댓글 고려사항
    /**
     * 1. 댓글은 메뉴와 사용자의 연관관계를 가진다.
     * 2. 댓글과 메뉴는 M:1 / 사용자도 M:1
     */
    @PostMapping("/{id}")
    public String addComment(@PathVariable("id") Long id,
                             @Validated @ModelAttribute("comment") CommentForm form,
                             BindingResult bindingResult,
                             @SessionAttribute("user") BaseUser sei) {
        log.info("form = {}", form);
        if (bindingResult.hasErrors()) {
            log.info("result = {} ", bindingResult.toString());
            return "redirect:/menus/" + id;
        }
        Student student = (Student) sei;
        Comment comment = new Comment(form);
        postService.addComment(student.getId(), comment, id);

        return "redirect:/menus/" + id;
    }

    @PostMapping("/comment/{id}")
    public String delComment(@PathVariable("id") Long id,
                             @SessionAttribute("user") BaseUser sei){
        //TODO comment - User 같이
        Optional<Comment> comment = commentRepository.findByIdWithUser(id, sei.getId());
        if(comment.isEmpty())
            return "/"; //TODO 잘못된 접근 처리하기
        commentRepository.delete(comment.get());
        return "redirect:/menus";
    }

}
