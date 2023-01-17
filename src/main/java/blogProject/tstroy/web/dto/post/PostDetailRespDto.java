package blogProject.tstroy.web.dto.post;

import blogProject.tstroy.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDetailRespDto {
    private Post post;
    private boolean isPageOwner;
    private boolean isLove;
    private Integer loveId;

}
