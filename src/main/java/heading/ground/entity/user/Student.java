package heading.ground.entity.user;

import heading.ground.entity.book.Book;
import heading.ground.entity.post.Comment;
import heading.ground.forms.user.BaseSignUp;
import heading.ground.forms.user.StudentForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Student extends BaseUser {

//    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "student_id")
//    private Long id;

//    private String loginId;
//
//    private String password;
//
//    private String name;
//
//    private String email;
//
//    private boolean isAdmin;

    @OneToMany(mappedBy = "writer",cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Book> books = new ArrayList<>();

    public Student(BaseSignUp std) {
            this.loginId = std.getLoginId();
            this.password = std.getPassword();
            this.name = std.getName();
            this.email = std.getEmail();
            this.phoneNumber = std.getPhoneNumber();
    }
}
