package heading.ground.service;

import heading.ground.entity.ImageFile;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import heading.ground.file.FileRepository;
import heading.ground.file.FileStore;
import heading.ground.forms.post.MenuForm;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//글과 댓글(리뷰)등을 관리하는 서비스
@Service
@RequiredArgsConstructor
public class PostService {
    private final SellerRepository sellerRepository;
    private final MenuRepository menuRepository;
    private final FileRepository fileRepository;
    private final FileStore fileStore;

    @Transactional
    public void addMenu(MenuForm form, Seller sessionSeller) throws IOException {
        Seller seller = sellerRepository.findById(sessionSeller.getId()).get();
        Menu menu = form.toEntity();

        MultipartFile image = form.getImage();
        if(!image.isEmpty()){ //이미지 있으면
            ImageFile imageFile = fileStore.storeFile(image);
            menu.addImage(imageFile);
        }
        menu.addSeller(seller);
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(Menu menu,MenuForm form) throws IOException {
        menu.updateField(form);
        //이미지 체크
        if(!form.getImage().isEmpty()){
            MultipartFile image = form.getImage();
            ImageFile imageFile = fileStore.storeFile(image);

            if(menu.getImage()!=null){ //기존 이미지가 있으면 삭제함.
                String imageId = menu.getImage().getId();
                fileRepository.deleteById(imageId);
                fileStore.deleteImage(menu.getImage().getStoreName());
            }
            menu.addImage(imageFile);
        }
    }

}
