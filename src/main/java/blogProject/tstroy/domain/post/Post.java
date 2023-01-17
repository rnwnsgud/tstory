package blogProject.tstroy.domain.post;

import blogProject.tstroy.domain.BaseEntity;
import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.user.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Post extends BaseEntity {

    @Column(length = 60, nullable = false)
    private String title;

    @Lob
    @Column(nullable = true)
    private String content;

    @Column(length = 200, nullable = true)
    private String thumbnail;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @JoinColumn(name = "categoryId")
    @ManyToOne
    private Category category;




}
