package heading.ground.dto;

import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import lombok.Data;

@Data
public class MenuDto {

    private Long id;
    private String name; //음식 이름
    private int price; //음식 가격

    private String desc; //음식 설명

    private String sources;//음식에 들어가는 재료, 굳이 엔티로 뽑을 필요 없음

    private int quantity;

    //메뉴가 소속된 가게
    private String seller;

    //storeName으로 저장 -> uuid이름
    private String image;

    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.desc = menu.getDesc();
        this.sources = menu.getSources();
        this.quantity = menu.getQuantity();
        this.seller = menu.getSeller().getName();
        if(menu.getImage()!=null){
            this.image = menu.getImage().getStoreName();
        }
    }
}
