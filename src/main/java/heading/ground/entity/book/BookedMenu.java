package heading.ground.entity.book;

import heading.ground.entity.post.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookedMenu {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int price;
    private int quantity;

    public BookedMenu(Menu menu, int quantity) {
        this.menu = menu;
        this.price = menu.getPrice();
        this.quantity = quantity;
    }
}
