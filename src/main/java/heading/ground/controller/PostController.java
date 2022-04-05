package heading.ground.controller;

import heading.ground.dto.MenuDto;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.forms.post.MenuForm;
import heading.ground.repository.post.MenuRepository;
import heading.ground.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String menuList(Model model) {
        List<Menu> all = menuRepository.findAll();
        List<MenuDto> menuDto = all.stream()
                .map(a -> new MenuDto(a))
                .collect(Collectors.toList());

        log.info("menu list = {}", all.size());
        model.addAttribute("menus", menuDto);
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

        return "redirect:/seller/account";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Menu menu = menuRepository.findById(id).get();
        MenuForm form = new MenuForm(menu);
        model.addAttribute("menu",form);

        return "post/menuForm";
    }

    @PostMapping("/{id}/edit")
    public String editMenu(@Validated @ModelAttribute("menu") MenuForm form,BindingResult bindingResult,
                           @PathVariable("id") Long id) throws IOException {
        Menu menu = menuRepository.findById(id).get();
        postService.updateMenu(menu,form);

        return "redirect:/seller/account";
    }


}
