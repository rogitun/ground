package heading.ground.entity.post;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class menu extends base {

    @Id @GeneratedValue
    private Long id;
    
    private String name; //음식 이름
    private int price; //음식 가격

    private String desc; //음식 설명

    private String sources;//음식에 들어가는 재료, 굳이 엔티로 뽑을 필요 없음

    private int quantity;

}
