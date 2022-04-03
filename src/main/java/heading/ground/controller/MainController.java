package heading.ground.controller;

import heading.ground.dto.SellerDto;
import heading.ground.entity.user.Seller;
import heading.ground.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SellerRepository sellerRepository;
    @GetMapping("/")
    public String home(Model model){
        List<Seller> all = sellerRepository.findAll();
        List<SellerDto> sellers = all.stream().map(s -> new SellerDto(s)).collect(Collectors.toList());
        model.addAttribute("seller",sellers);
        return "main/home";
    }

    @GetMapping("/menus")
    public String menus(){
        return "main/menus";
    }
}
