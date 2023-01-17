package blogProject.tstroy.domain.user;

import blogProject.tstroy.domain.BaseEntity;
import blogProject.tstroy.domain.Visit.Visit;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class User extends BaseEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 60, nullable = false)
    private String email;

    @Column(nullable = true)
    private String profileImg;


    @Builder
    public User(String username, String password, String email, String profileImg) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profileImg = profileImg;
    }
}
