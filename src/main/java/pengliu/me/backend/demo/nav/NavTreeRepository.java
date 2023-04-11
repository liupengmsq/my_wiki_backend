package pengliu.me.backend.demo.nav;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface NavTreeRepository  extends JpaRepository<NavTreeNode, Long> {

    @Modifying
    @Query("update NavTreeNode n set n.title = ?2 where n.id= ?1")
    int updateTitleById(Long id, String title);

    List<NavTreeNode> findByIsRoot(Boolean root);
}
