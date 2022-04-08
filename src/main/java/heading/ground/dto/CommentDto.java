package heading.ground.dto;


import heading.ground.entity.post.Comment;
import lombok.Data;



@Data
public class CommentDto {

    private Long id;
    private String writer;
    private String desc;
    private int star;

    public CommentDto(Comment c) {
        this.id = c.getId();
        this.writer = c.getWriter().getName();
        this.desc = c.getDesc();
        this.star = c.getStar();
    }
}
