package heading.ground.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class ImageFile {

    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    private String id;

    private String originName;

    private String storeName;

    public ImageFile(String uploadName, String storeName) {
        this.originName = uploadName;
        this.storeName = storeName;
    }
}
