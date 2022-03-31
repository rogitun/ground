package heading.ground.entity.user;

import heading.ground.forms.SellerSignUpForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class Seller extends BaseUser{

    @Id @GeneratedValue
    private Long id;


    private String loginId;
    private String password;

    private String name; //가게 이름

    private int seats; //좌석 수

    private String phoneNumber; //가게 전화번호

    @Embedded
    private Address address; //주소

    //가게 정보
    private String SellerId; //사업자 번호

    private String email;

    private boolean isAdmin;

    public Seller(SellerSignUpForm sf) {
        this.loginId = sf.getLoginId();
        this.password = sf.getPassword();
        this.name = sf.getName();
        this.phoneNumber = sf.getPhoneNumber();
        this.email = sf.getEmail();
        this.isAdmin = true;
    }
}
