package blogProject.tstroy.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

//    @Query(value = "SELECT * from Category where userId = :userId", nativeQuery = true)
    List<Category> findByUserId(Integer userId);
}
