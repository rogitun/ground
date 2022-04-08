package heading.ground.controller;

import heading.ground.dto.Paging;
import heading.ground.dto.SellerDto;
import heading.ground.file.FileStore;
import heading.ground.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final UserService userService;
    private final FileStore fileStore;

    @GetMapping("/")
    public String home(Model model,Pageable pageable){
        int page = (pageable.getPageNumber()==0)? 0: (pageable.getPageNumber()-1);
        Page<SellerDto> pages = userService.page(page, 6);
        Paging paging = userService.pageTemp(pages);


        model.addAttribute("seller",pages);
        model.addAttribute("paging",paging);
        log.info("number = {}",pages.getNumber());
        log.info("total = {}",pages.getTotalPages());

        return "main/home";
    }


    @ResponseBody
    @GetMapping("/image/{image}")
    public Resource showImage(@PathVariable String image) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(image));
    }
}
