package heading.ground.entity.post;

import heading.ground.entity.ImageFile;
import heading.ground.entity.user.Seller;
import heading.ground.forms.post.MenuForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends base {

    @Id @GeneratedValue
    private Long id;
    
    private String name; //음식 이름
    private int price; //음식 가격

    private String desc; //음식 설명

    @Column(columnDefinition = "text")
    private String sources;//음식에 들어가는 재료, 굳이 엔티로 뽑을 필요 없음

    private int quantity;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private ImageFile image;

    //메뉴가 소속된 가게
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public Menu(MenuForm form) {
        updateField(form);
    }

    public void addSeller(Seller seller){
        this.seller = seller;
        seller.getMenus().add(this);
    }

    public void addImage(ImageFile image){
        this.image = image;
    }

    public void updateField(MenuForm form){
        this.name = form.getName();
        this.price = form.getPrice();
        this.desc = form.getDesc();
        this.sources = form.getSources();
        this.quantity = form.getQuantity();
    }

}
