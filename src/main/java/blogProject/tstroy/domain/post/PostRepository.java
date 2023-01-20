package blogProject.tstroy.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * from Post where userId = :userId ORDER by id desc", nativeQuery = true)
    Page<Post> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    // select * from Post where userId=:userId AND categoryId=:categoryId ORDER by id desc
    Page<Post> findByUserIdAndCategoryId(Integer userId, Integer categoryId, Pageable pageable);
}
