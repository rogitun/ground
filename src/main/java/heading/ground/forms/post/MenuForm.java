package heading.ground.forms.post;

import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Data
public class MenuForm {

    @NotBlank
    private String name; //음식 이름

    @Range(min = 100,max = 999999)
    private int price; //음식 가격

    @NotBlank
    private String desc; //음식 설명

    private String sources;//음식에 들어가는 재료, 굳이 엔티로 뽑을 필요 없음

    //하루 제한 수량
    private int quantity;

    public Menu toEntity(){
        return new Menu(this);
    }

}
