package pengliu.me.backend.demo.wiki;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface WikiImageRepository extends JpaRepository<WikiImage, Long> {
    long deleteByFileName(String fileName);

    List<WikiImage> findByFileNameContaining(String fileName);

}
