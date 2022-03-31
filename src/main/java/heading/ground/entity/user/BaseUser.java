package heading.ground.entity.user;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseUser {

    private boolean isAdmin;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime lastModified;



}
