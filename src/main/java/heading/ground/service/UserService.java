package heading.ground.service;

import heading.ground.dto.Paging;
import heading.ground.dto.SellerDto;
import heading.ground.entity.ImageFile;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.file.FileRepository;
import heading.ground.file.FileStore;
import heading.ground.forms.user.SellerEditForm;
import heading.ground.repository.user.SellerRepository;
import heading.ground.repository.user.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final StudentRepository studentRepository;
    private final SellerRepository sellerRepository;
    private final FileRepository fileRepository;

    private final FileStore fileStore;

    public BaseUser logIn(String loginId, String password) {
        Optional<Student> isStudent = studentRepository.findByLoginId(loginId);
        Optional<Seller> isSeller = sellerRepository.findByLoginId(loginId);
        if (isStudent.isEmpty() && isSeller.isEmpty()) {
            return null;
        }
        if (isStudent.isPresent()) {
            Student student = isStudent.get();
            if (student.getPassword().equals(password))
                return student;
        }
        if (isSeller.isPresent()) {
            Seller seller = isSeller.get();
            if (seller.getPassword().equals(password))
                return seller;
        }
        return null;
    }

    @Value("${file.dir}")
    private String path;

    @Transactional
    public Seller updateSeller(Long id, SellerEditForm form) throws IOException {
        Seller seller = sellerRepository.findById(id).get();
        seller.update(form);
        if (!form.getImageFile().isEmpty()) {
            log.info("service Update = {}", form);
            if (seller.getImageFile() != null) { //이미 가진 사진이 있다면? -> 해당 사진 엔티티 찾고 파일 삭제 후 엔티티 삭제
                //storeName으로 delete
                String storeName = seller.getImageFile().getStoreName();
                fileStore.deleteImage(storeName);
                fileRepository.deleteByStoreName(storeName);
            }
            ImageFile imageFile = fileStore.storeFile(form.getImageFile());
            seller.updateImage(imageFile);
        }
        //TODO 사진이 다르면 기존의 사진 delete -> 해당 사진 엔티티 찾고 파일 삭제 후 엔티티 삭제


        return seller;
    }

    public Page<SellerDto> page(int s, int size) {
        PageRequest pageRequest = PageRequest.of(s, size);
        Page<Seller> all = sellerRepository.findAll(pageRequest);
        return all.map(se -> new SellerDto(se));
    }

    public Paging pageTemp(Page<SellerDto> page){
        return new Paging(page.getTotalPages(), page.getNumber());
    }
}
