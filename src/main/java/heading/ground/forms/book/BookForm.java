package heading.ground.forms.book;

import heading.ground.entity.post.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookForm {
    //메뉴들 쫙 보여줘야함
    //그 메뉴에 맞는 수량
    //포장인지 혹은 매장인지
    private String menu;
    private int quantity;


    public BookForm() {
    }

    public BookForm(String name) {
        menu=name;
    }
//
//    public BookForm(String menuName, int quantity) {
//        this.menuName = menuName;
//        this.quantity = quantity;
//    }
}
