package heading.ground.service;

import heading.ground.dto.Paging;
import heading.ground.dto.post.MenuDto;
import heading.ground.dto.user.SellerDto;
import heading.ground.entity.ImageFile;
import heading.ground.entity.post.Menu;
import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.file.FileRepository;
import heading.ground.file.FileStore;
import heading.ground.forms.user.SellerEditForm;
import heading.ground.forms.user.UserEditForm;
import heading.ground.repository.post.MenuRepository;
import heading.ground.repository.user.SellerRepository;
import heading.ground.repository.user.StudentRepository;
import heading.ground.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final StudentRepository studentRepository;
    private final SellerRepository sellerRepository;
    private final FileRepository fileRepository;
    private final FileStore fileStore;
    private final UserRepository userRepository;

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
        Seller seller = userRepository.findSellerWithImage(id);

        if(form.getDesc().isBlank() && form.getImageFile()==null && form.getSellerId().isBlank())
            seller.update(form);

        else{
            seller.updateSeller(form);
            if (seller.getImageFile() != null) { //?????? ?????? ????????? ?????????? -> ?????? ?????? ????????? ?????? ?????? ?????? ??? ????????? ??????
                String storeName = seller.getImageFile().getStoreName();
                fileRepository.deleteByStoreName(storeName);
            }
            ImageFile imageFile = fileStore.storeFile(form.getImageFile());
            seller.updateImage(imageFile);
        }
        return seller;
    }

    @Transactional
    public Student updateStudent(Long id, UserEditForm form){
        Student student = userRepository.findStudentById(id);
        student.update(form);
        return student;
    }


    public Page<SellerDto> page(int s, int size) {
        PageRequest pageRequest = PageRequest.of(s, size);
        Page<Seller> all = userRepository.findAll(pageRequest);
        return all.map(se -> new SellerDto(se));
    }

    public Paging pageTemp(Page<SellerDto> page){
        return new Paging(page.getTotalPages(), page.getNumber());
    }

}
