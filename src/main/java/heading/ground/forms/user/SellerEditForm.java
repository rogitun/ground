package heading.ground.forms.user;

import heading.ground.entity.user.Seller;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SellerEditForm {

    private Long id;
    @NotBlank
    @Length(max = 16)
    private String name; //가게 이름

    @NotBlank
    @Pattern(regexp = "^\\d{2,4}-\\d{3,4}-\\d{4}$",message = "00/000-0000-0000 형태로 입력해주세요")
    private String phoneNumber; //가게 전화번호

    @Length(max = 256)
    @NotBlank
    private String desc;

    private MultipartFile imageFile;

    private String image_present;

    @NotBlank
    private String sellerId;

    public SellerEditForm(){}
    public SellerEditForm(Seller seller) {
        this.id = seller.getId();
        this.name = seller.getName();
        this.phoneNumber = seller.getPhoneNumber();
        this.desc = seller.getDesc();
        this.sellerId = seller.getCompanyId();
        if(seller.getImageFile()!=null)
            this.image_present = seller.getImageFile().getOriginName();
    }
}
