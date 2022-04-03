package heading.ground.dto;

import heading.ground.entity.post.Menu;
import heading.ground.entity.user.Seller;
import lombok.Data;

@Data
public class MenuDto {

    private String name; //음식 이름
    private int price; //음식 가격

    private String desc; //음식 설명

    private String sources;//음식에 들어가는 재료, 굳이 엔티로 뽑을 필요 없음

    private int quantity;

    //메뉴가 소속된 가게
    private String seller;

    public MenuDto(Menu menu) {
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.desc = menu.getDesc();
        this.sources = menu.getSources();
        this.quantity = menu.getQuantity();
        this.seller = menu.getSeller().getSellerId();
    }
}
