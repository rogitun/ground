package heading.ground.dto.user;

import lombok.Data;

@Data
public class StudentDto {

    private String name;

    private String email;

    public StudentDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public StudentDto(String name) {
        this.name = name;
    }
}
