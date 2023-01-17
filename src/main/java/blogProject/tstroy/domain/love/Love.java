package blogProject.tstroy.domain.love;

import blogProject.tstroy.domain.BaseEntity;
import blogProject.tstroy.domain.post.Post;
import blogProject.tstroy.domain.user.User;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "love_uk", columnNames = { "postId", "userId" })
})
public class Love extends BaseEntity {

    @JoinColumn(name = "postId")
    @ManyToOne
    private Post post;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;
}
