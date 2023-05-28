package pengliu.me.backend.demo.wiki;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface WikiCategoryRepository extends JpaRepository<WikiCategory, Long> {
    List<WikiCategory> findByCategoryName(String categoryName);

    List<WikiCategory> findByIsDefault(Boolean isDefault);

    List<WikiCategory> findByIsBlog(Boolean isBlog);

}
