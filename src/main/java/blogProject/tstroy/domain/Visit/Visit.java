package blogProject.tstroy.domain.Visit;

import blogProject.tstroy.domain.BaseEntity;
import blogProject.tstroy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Visit extends BaseEntity {

    // User 회원가입시 방문자 카운트 0으로 초기화 해두자.

    @Column(nullable = false)
    private Long totalCount;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @Builder
    public Visit(Long totalCount, User user) {
        this.totalCount = totalCount;
        this.user = user;
    }
}
