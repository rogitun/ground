package heading.ground.controller;

import heading.ground.dto.post.CommentDto;
import heading.ground.dto.post.MenuDto;
import heading.ground.dto.Paging;
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
import java.io.IOException;
import java.util.List;

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
    public String menuForm(Model model) { //메뉴 폼
        model.addAttribute("menu", new MenuForm());
        return "post/menuForm";
    }

    @PostMapping("/add-menu")
    public String addMenu(@Validated @ModelAttribute("menu") MenuForm form, BindingResult bindingResult,
                          @SessionAttribute(name = "user", required = false) BaseUser seller) throws IOException { //메뉴 폼
        if (bindingResult.hasErrors()) {
            return "post/menuForm";
        }
        Seller se = (Seller) seller;
        postService.addMenu(form, se);

        return "redirect:/profile";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Menu menu = menuRepository.findById(id).get();
        MenuForm form = new MenuForm(menu);
        model.addAttribute("menu", form);

        return "post/menuForm";
    }

    @PostMapping("/{id}/edit")
    public String editMenu(@Validated @ModelAttribute("menu") MenuForm form, BindingResult bindingResult,
                           @PathVariable("id") Long id) throws IOException {
        Menu menu = menuRepository.findById(id).get();
        postService.updateMenu(menu, form);
        return "redirect:/profile";
    }

    @GetMapping("/{id}")
    public String singleMenu(@PathVariable("id") Long id, Model model,
                             @SessionAttribute("user") BaseUser user) {
        CommentForm commentForm = new CommentForm();
        if (user instanceof Student)
            commentForm.setStudent(true);

        Menu entity = menuRepository.findById(id).get();
        MenuDto menu = new MenuDto(entity);

        //댓글 목록 가져오기
        List<Comment> comments = commentRepository.findByMenuId(entity.getId());
        if (comments != null) {
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

    @GetMapping("/del/{id}")
    public String delCommentForm(@PathVariable("id") Long id,Model model){
        Comment comment = commentRepository.findById(id).get();
        CommentDto commentDto = new CommentDto(comment);
        model.addAttribute("del",commentDto);

        return "post/delete";
    }

    @PostMapping("/del/{id}")
    public String delComment(@PathVariable("id") Long id){
        commentRepository.deleteById(id);
        return "redirect:/menus";
    }

}
