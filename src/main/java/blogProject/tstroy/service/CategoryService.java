package blogProject.tstroy.service;

import blogProject.tstroy.domain.category.Category;
import blogProject.tstroy.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void registerCategory(Category category) {
        categoryRepository.save(category);
    }
}
