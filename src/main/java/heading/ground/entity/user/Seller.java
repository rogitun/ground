package heading.ground.entity.user;

import heading.ground.forms.SellerEditForm;
import heading.ground.forms.SellerSignUpForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
//@ToString
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

    //TODO 이미지 파일 추가
    
    //가게 정보
    private String SellerId; //사업자 번호

    private String email;

    private boolean isAdmin;

    @Column(columnDefinition = "text")
    private String desc;

    public Seller(SellerSignUpForm sf) {
        this.loginId = sf.getLoginId();
        this.password = sf.getPassword();
        this.name = sf.getName();
        this.phoneNumber = sf.getPhoneNumber();
        this.email = sf.getEmail();
        this.desc = sf.getDesc();
        this.isAdmin = true;
    }

    public void update(SellerEditForm form){
        this.name = form.getName();
        this.phoneNumber = form.getPhoneNumber();
        this.seats = form.getSeats();
        this.desc = form.getDesc();
        this.email = form.getEmail();
        this.SellerId = form.getSellerId();
    }
}
