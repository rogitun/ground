package heading.ground.dto.book;

import heading.ground.entity.book.BookedMenu;
import lombok.Data;

@Data
public class BookedMenuDto {

    private String menu;

    private int price;

    private int quantity;

    public BookedMenuDto(BookedMenu bm) {
        menu = bm.getMenu().getName();
        price = bm.getPrice();
        quantity = bm.getQuantity();
    }
}
