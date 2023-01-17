package blogProject.tstroy.domain.Visit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    Optional<Visit> findByUserId(Integer userId);
}
