package heading.ground.forms;

import heading.ground.entity.user.Address;
import heading.ground.entity.user.Seller;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class SellerSignUpForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
    @NotBlank
    private String password2;

    @NotBlank
    private String name; //가게 이름

    @Length(max = 13)
    private String phoneNumber; //가게 전화번호

    @Email
    @NotBlank
    private String email;

    public Seller toEntity(){
        Seller seller = new Seller(this);
        return seller;
    }

}
