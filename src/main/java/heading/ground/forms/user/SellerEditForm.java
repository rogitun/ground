package heading.ground.forms.user;

import heading.ground.entity.ImageFile;
import heading.ground.entity.user.Seller;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SellerEditForm {

    @NotBlank
    private String name; //가게 이름

    @Length(max = 13)
    @NotBlank
    private String phoneNumber; //가게 전화번호

    private int seats; //설정 안해도 ㄱㅊ;

    @Length(max = 256)
    @NotBlank
    private String desc;

    private MultipartFile imageFile;

    @Email
    private String email;

    @NotBlank
    private String sellerId;

    //TODO
    //이미지 필드 필요합니다.

    public SellerEditForm(){}
    public SellerEditForm(Seller seller) {
        this.name = seller.getName();
        this.phoneNumber = seller.getPhoneNumber();
        this.seats = seller.getSeats();
        this.desc = seller.getDesc();
        this.email = seller.getEmail();
        this.sellerId = seller.getSellerId();
    }
}
