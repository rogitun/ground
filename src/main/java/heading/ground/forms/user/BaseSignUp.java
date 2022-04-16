package heading.ground.forms.user;

import heading.ground.entity.user.Seller;
import heading.ground.entity.user.Student;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class BaseSignUp {

    @NotBlank
    @Length(max = 16)
    private  String loginId;

    @NotBlank
    @Length(max = 12)
    private  String password;
    @NotBlank
    @Length(max = 12)
    private  String password2;

    @NotBlank
    @Length(max = 16)
    private  String name; //가게 이름 혹은 학생 별명

    @NotBlank
    @Email
    private  String email;

    @NotBlank
    @Pattern(regexp = "^\\d{2,4}-\\d{3,4}-\\d{4}$",message = "00/000-0000-0000 형태로 입력해주세요")
    private  String phoneNumber; //전화번호

    private String companyId;

    public Seller toSeller(BaseSignUp form){
        return new Seller(form);
    }

    public Student toStudent(BaseSignUp form){
        return new Student(form);
    }

    //TODO Test
    public BaseSignUp(String loginId, String password, String password2, String name, String email, String phoneNumber, String companyId) {
        this.loginId = loginId;
        this.password = password;
        this.password2 = password2;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyId = companyId;
    }
}
