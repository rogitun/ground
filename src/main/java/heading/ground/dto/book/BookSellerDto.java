package heading.ground.dto.book;

import heading.ground.entity.user.Seller;
import lombok.Data;

@Data
public class BookSellerDto {

    private Long id;

    private String name;

    private String photo;

    public BookSellerDto(Seller seller) {
        id = seller.getId();
        name = seller.getName();
        if(seller.getImageFile()!=null)
            photo = seller.getImageFile().getStoreName();
    }
}
