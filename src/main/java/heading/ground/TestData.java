package heading.ground;

import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.user.SellerSignUpForm;
import heading.ground.forms.user.StudentForm;
import heading.ground.repository.user.SellerRepository;
import heading.ground.repository.user.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestData {

    private final StudentRepository su;
    private final SellerRepository seu;

    @PostConstruct
    public void init(){
        StudentForm s = new StudentForm("kunyjf","1234","1234","test","test@naver.com",false);
        SellerSignUpForm se = new SellerSignUpForm("test", "1234", "Test_KFC", "02-000-000", "eee@eee.com");
        Seller seller = se.toEntity();
        Student student = s.toEntity();
        seu.save(seller);
        su.save(student);
    }
}
