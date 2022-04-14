package heading.ground.dto.book;

import heading.ground.entity.post.Menu;
import lombok.Data;

@Data
public class MenuListDto {

    private String name;

    private int price;

    public MenuListDto(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public MenuListDto(Menu menu) {
        name = menu.getName();
        price = menu.getPrice();
    }
}
