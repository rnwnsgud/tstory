package blogProject.tstroy.domain.category;

import blogProject.tstroy.domain.BaseEntity;
import blogProject.tstroy.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "category_uk", columnNames = { "title", "userId" })
})
public class Category extends BaseEntity {

    @Column(length = 60, nullable = false)
    private String title;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    public Category(String title, User user) {
        this.title = title;
        this.user = user;
    }
}
