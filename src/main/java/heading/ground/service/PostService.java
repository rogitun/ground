package heading.ground.service;

import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//글과 댓글(리뷰)등을 관리하는 서비스
@Service
@RequiredArgsConstructor
public class PostService {
    private final SellerRepository sellerRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void addMenu(Menu menu, Seller sessionSeller){
        Seller seller = sellerRepository.findById(sessionSeller.getId()).get();
        menu.addSeller(seller);
    }

}
