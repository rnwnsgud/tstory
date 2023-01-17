package blogProject.tstroy.web.dto.category;

import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.user.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryReqDto {

    @Size(min = 1, max = 60)
    @NotBlank
    private String title;

    public Category toEntity(User principal) {
        Category category = new Category();
        category.setTitle(title);
        category.setUser(principal);
        return category;
    }
}
