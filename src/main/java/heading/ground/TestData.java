package heading.ground;

import heading.ground.entity.user.Student;
import heading.ground.forms.StudentForm;
import heading.ground.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestData {

    private final StudentRepository su;

    @PostConstruct
    public void init(){
        StudentForm s = new StudentForm("kunyjf","1234","1234","test","test@naver.com",false);
        Student student = s.toEntity();
        su.save(student);
    }
}
