package heading.ground.entity.post;

import heading.ground.entity.ImageFile;
import heading.ground.entity.Base;
import heading.ground.entity.book.BookedMenu;
import heading.ground.entity.user.Seller;
import heading.ground.forms.post.MenuForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends Base {

    @Id @GeneratedValue
    @Column(name = "menu_id")
    private Long id;
    
    private String name; //음식 이름
    private int price; //음식 가격

    private String desc; //음식 설명

    @Column(columnDefinition = "text")
    private String sources;//음식에 들어가는 재료, 굳이 엔티로 뽑을 필요 없음

    private int commentNumber;

    private int star;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private ImageFile image;

    //메뉴가 소속된 가게
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "menu",cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "menu",cascade = CascadeType.REMOVE) //필요한가?
    private List<BookedMenu> bookedMenus = new ArrayList<>();



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
    }

    public void addStar(int num) {
        star+=num; commentNumber++;
    }

}
