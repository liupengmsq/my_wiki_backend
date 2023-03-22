package pengliu.me.backend.demo.nav;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NavTreeRepository  extends JpaRepository<NavTreeNode, Integer> {

    @Modifying
    @Query("update NavTreeNode n set n.title = ?2 where n.id= ?1")
    int updateTitleById(Integer id, String title);
}
