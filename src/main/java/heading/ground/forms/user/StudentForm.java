package heading.ground.forms.user;

import heading.ground.entity.user.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

//가입용
@Data
@EqualsAndHashCode(callSuper = false)
public class StudentForm {

//    public Student toEntity(){
//        return new Student(this);
//    }

//    public StudentForm() {
//    }
//
//    //테스트용
//    public StudentForm(String loginId, String password,
//                       String password2, String name,
//                       String email) {
//        this.loginId = loginId;
//        this.password = password;
//        this.password2 = password2;
//        this.name = name;
//        this.email = email;
//    }
}
