package blogProject.tstroy.domain.love;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Integer> {

    // select * from Love where userId= :userId AND postId = :postID
    Optional<Love> findByUserIdAndPostId(Integer userId, Integer postId);
}
