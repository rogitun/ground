package heading.ground.entity.user;

import heading.ground.forms.StudentForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Student extends BaseUser {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    private boolean isAdmin;


    public Student(Object std) {
        if(std instanceof StudentForm) {
            StudentForm stdForm = (StudentForm) std;
            this.loginId = stdForm.getLoginId();
            this.password = stdForm.getPassword();
            this.name = stdForm.getName();
            this.email = stdForm.getEmail();
            this.isAdmin = false;
        }
    }
}
