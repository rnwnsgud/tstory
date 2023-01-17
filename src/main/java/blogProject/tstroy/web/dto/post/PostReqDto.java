package blogProject.tstroy.web.dto.post;

import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.post.Post;
import blogProject.tstroy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PostReqDto {

    @NotBlank
    private Integer categoryId;

    @Size(min = 1, max = 60)
    @NotBlank
    private String title;

    private MultipartFile thumbnailFile;

    @NotNull
    private String content;

    @Builder
    public PostReqDto(@NotBlank Integer categoryId, @Size(min = 1, max = 60) @NotBlank String title, MultipartFile thumbnailFile, @NotNull String content) {
        this.categoryId = categoryId;
        this.title = title;
        this.thumbnailFile = thumbnailFile;
        this.content = content;
    }

    public Post toEntity(String thumbnail, User principal, Category category) {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setThumbnail(thumbnail);
        post.setUser(principal);
        post.setCategory(category);
        return post;
    }


}
