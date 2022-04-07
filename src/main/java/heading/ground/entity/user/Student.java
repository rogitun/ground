package heading.ground.entity.user;

import heading.ground.entity.post.Comment;
import heading.ground.forms.user.StudentForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Student extends BaseUser {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "student_id")
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    private boolean isAdmin;

    @OneToMany(mappedBy = "writer",cascade = CascadeType.REMOVE)
    private List<Comment> comments;


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
