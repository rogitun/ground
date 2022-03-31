package heading.ground.forms;

import heading.ground.entity.user.Student;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

//가입용
@Data
public class StudentForm {

    private Long id;

    @Length(max = 10)
    @NotBlank(message = "ㄴㄴㄴㄴ")
    private String loginId;

    @NotBlank
    @Length(max = 12)
    private String password;
    @NotBlank
    @Length(max = 12)
    private String password2;

    @NotBlank
    @Length(max = 10)
    private String name;

    @NotBlank
    @Email
    private String Email;

    private boolean isAdmin;

    public Student toEntity(){
        return new Student(this);
    }

    public StudentForm() {
    }

    //테스트용
    public StudentForm(String loginId, String password,
                       String password2, String name,
                       String email, boolean isAdmin) {
        this.loginId = loginId;
        this.password = password;
        this.password2 = password2;
        this.name = name;
        Email = email;
        this.isAdmin = isAdmin;
    }
}
