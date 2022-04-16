package heading.ground.forms.user;

import heading.ground.entity.user.Address;
import heading.ground.entity.user.Seller;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = false)
public class SellerSignUpForm {

    @NotBlank
    private String companyId;

//    public Seller toEntity(){
//        Seller seller = new Seller(this);
//        return seller;
//    }
//
//    public SellerSignUpForm() {
//    }
//
//    public SellerSignUpForm(String loginId, String password, String name, String phoneNumber, String email) {
//        this.loginId = loginId;
//        this.password = password;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//    }
}
