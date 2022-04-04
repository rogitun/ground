package heading.ground.dto;

import heading.ground.entity.user.Address;
import heading.ground.entity.user.Seller;
import lombok.Data;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SellerDto {

    private Long id;
    private String name; //가게 이름
    private int seats; //좌석 수

    private String phoneNumber;

    private Address address;

    private String sellerId;

    private String email;

    private String desc;

    private List<MenuDto> menus = new ArrayList<>();

    private String photo;

    public SellerDto(Seller seller) {
        this.id = seller.getId();
        this.name = seller.getName();
        this.seats = seller.getSeats();
        this.phoneNumber = seller.getPhoneNumber();
        this.address = seller.getAddress();
        this.sellerId = seller.getSellerId();
        this.email = seller.getEmail();
        this.desc = seller.getDesc();
        this.photo = seller.getPhoto();
        if(seller.getMenus().size()>0) {
            menus = seller.getMenus()
                    .stream()
                    .map(m -> new MenuDto(m))
                    .collect(Collectors.toList());
        }
    }

    public String getPhotoPath(){
        if(photo==null) return null;

        return "/src/main/resources/static/files/" + id + "/" + photo;
    }
}
