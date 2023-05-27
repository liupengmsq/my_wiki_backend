package pengliu.me.backend.demo.wiki;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public interface WikiRepository extends JpaRepository<Wiki, Long> {
    // The EntityManager doesn't flush change automatically by default. You should use the following option with your statement of query:
    @Modifying(clearAutomatically = true)
    @Query("update Wiki w set w.accessDateTime = ?2, w.pageViewedNumber = ?3 where w.id= ?1")
    int updateAccessDateTimeAndPageViewedNumberById(Long id, Date accessDateTime, Integer pageViewedNumber);

    List<Wiki> findByTitleContainingIgnoreCaseOrMarkdownContentContainingIgnoreCase(String title, String markdownContent);

    Page<Wiki> findAllByOrderByCreatedDateTimeDesc(Pageable pageable);

    List<Wiki> findFirst20ByOrderByCreatedDateTimeDesc();

    List<Wiki> findFirst20ByOrderByPageViewedNumberDesc();
}
