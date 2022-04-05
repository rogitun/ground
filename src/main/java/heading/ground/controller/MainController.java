package heading.ground.controller;

import heading.ground.dto.SellerDto;
import heading.ground.entity.user.Seller;
import heading.ground.file.FileStore;
import heading.ground.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SellerRepository sellerRepository;
    private final FileStore fileStore;

    @GetMapping("/")
    public String home(Model model){
        List<Seller> all = sellerRepository.findAll();
        List<SellerDto> sellers = all.stream().map(s -> new SellerDto(s)).collect(Collectors.toList());
        model.addAttribute("seller",sellers);
        return "main/home";
    }

    @ResponseBody
    @GetMapping("/image/{image}")
    public Resource showImage(@PathVariable String image) throws MalformedURLException {
        return new UrlResource("file:"+fileStore.getFullPath(image));
    }
}
