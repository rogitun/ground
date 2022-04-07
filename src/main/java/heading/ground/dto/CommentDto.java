package heading.ground.dto;


import heading.ground.entity.post.Comment;
import lombok.Data;



@Data
public class CommentDto {

    private String writer;
    private String desc;
    private int star;

    public CommentDto(Comment c) {
        this.writer = c.getWriter().getName();
        this.desc = c.getDesc();
        this.star = c.getStar();
    }
}
