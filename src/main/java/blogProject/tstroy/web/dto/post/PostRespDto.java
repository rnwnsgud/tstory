package blogProject.tstroy.web.dto.post;

import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostRespDto {
    private Page<Post> posts;
    private List<Category> categories;
    private Integer userId;
    private Integer prev;
    private Integer next;
    private List<Integer> pageNumbers;
    private Long totalCount;
}
