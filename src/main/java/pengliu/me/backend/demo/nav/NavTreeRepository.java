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

    @Query(value = "SELECT * FROM nav_node WHERE category_type_id = ?1 and is_root = 1", nativeQuery = true)
    List<NavTreeNode> findRootNodeByWikiCategoryId(Long categoryTypeId);

    @Query(value = "SELECT * FROM nav_node WHERE category_type_id = ?1", nativeQuery = true)
    List<NavTreeNode> findByWikiCategoryId(Long categoryTypeId);
}
