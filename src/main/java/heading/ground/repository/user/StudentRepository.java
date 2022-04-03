package heading.ground.repository.user;

import heading.ground.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student> findByLoginId(String loginId);

}
