package heading.ground.entity.user;

import heading.ground.entity.ImageFile;
import heading.ground.entity.book.Book;
import heading.ground.entity.post.Menu;
import heading.ground.forms.user.SellerEditForm;
import heading.ground.forms.user.SellerSignUpForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Seller extends BaseUser{

//    @Id
//    @GeneratedValue
//    private Long id;

//    private String loginId;
//    private String password;

    //    private String email;
//    private boolean isAdmin;

    private String name; //가게 이름

    private int seats; //좌석 수

    private String phoneNumber; //가게 전화번호

    @Embedded
    private Address address; //주소

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private ImageFile imageFile;

    //TODO 이미지 파일 추가
    
    //가게 정보
    private String SellerId; //사업자 번호


    @Column(columnDefinition = "text")
    private String desc;

    //가게가 가진 메뉴를 연관
    @OneToMany(mappedBy = "seller",cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<Book> books = new ArrayList<>();

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

    public void updateImage(ImageFile image){
        this.imageFile = image;
    }


}
