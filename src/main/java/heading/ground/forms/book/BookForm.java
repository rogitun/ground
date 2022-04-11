package heading.ground.forms.book;

import heading.ground.entity.post.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;



@Data
public class BookForm {
    //인원, 결제타입, 방문타입

    List<MenuSet> arr = new ArrayList<>();
    private String type;
    private String payment;
    private int number;


    public List<MenuSet> returnArr(){
        return arr;
    }
}
