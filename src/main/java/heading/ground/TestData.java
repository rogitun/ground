package heading.ground;

import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import heading.ground.forms.post.MenuForm;
import heading.ground.forms.user.SellerSignUpForm;
import heading.ground.forms.user.StudentForm;
import heading.ground.repository.post.MenuRepository;
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
    private final MenuRepository mu;

    @PostConstruct
    public void init(){

        for(int i=0;i<10;i++){
            StudentForm s = new StudentForm("kunyjf"+i,"1234","1234","test_"+i,"test@naver.com",false);
            SellerSignUpForm se = new SellerSignUpForm("test"+i, "1234", i+"_Test_KFC", "02-000-000", "eee@eee.com");
            Menu menu = new MenuForm("김치전_"+i, 4500, "막걸리와 함께", "김치랑 밀가루").toEntity();
            Menu menu2 = new MenuForm("닭강정_"+i, 6500, "춘천닭강정", "밀가루랑 닭").toEntity();
            Seller seller = se.toEntity();
            Student student = s.toEntity();

            seu.save(seller);
            menu.addSeller(seller);
            menu2.addSeller(seller);
            mu.save(menu);
            mu.save(menu2);
            su.save(student);
        }

    }
}
