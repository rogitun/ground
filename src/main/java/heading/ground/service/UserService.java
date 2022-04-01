package heading.ground.service;

import heading.ground.entity.user.BaseUser;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.SellerEditForm;
import heading.ground.repository.SellerRepository;
import heading.ground.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final StudentRepository studentRepository;
    private final SellerRepository sellerRepository;

    public BaseUser logIn(String loginId, String password){
        Optional<Student> isStudent = studentRepository.findByLoginId(loginId);
        Optional<Seller> isSeller = sellerRepository.findByLoginId(loginId);
        if(isStudent.isEmpty() && isSeller.isEmpty()){
            return null;
        }
        if(isStudent.isPresent()){
            Student student = isStudent.get();
            if(student.getPassword().equals(password))
                return student;
        }
        if(isSeller.isPresent()){
            Seller seller = isSeller.get();
            if(seller.getPassword().equals(password))
                return seller;
        }
        return null;
    }

    @Transactional
    public Seller updateSeller(Long id, SellerEditForm form){
        Seller seller = sellerRepository.findById(id).get();
        seller.update(form);

        return seller;
    }
}
