package pengliu.me.backend.demo.wiki;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pengliu.me.backend.demo.nav.NavTreeNode;

import javax.persistence.*;
import java.util.Set;
import java.util.Date;

/**
 * CREATE TABLE `wiki_category` (
 *   `id` int NOT NULL AUTO_INCREMENT,
 *   `category_name` varchar(500) NOT NULL,
 *   `nav_tree_root_id` int DEFAULT NULL,
 *   `created_datetime` datetime NOT NULL,
 *   `updated_datetime` datetime NOT NULL,
 *   PRIMARY KEY (`id`),
 *   KEY `nav_root_id_idx` (`nav_tree_root_id`),
 *   CONSTRAINT `nav_root_id` FOREIGN KEY (`nav_tree_root_id`) REFERENCES `nav_node` (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
@Entity
@Table(name = "wiki_category")
public class WikiCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, length = 500)
    private String categoryName;

    @CreationTimestamp
    @Column(name = "created_datetime", nullable = false, columnDefinition="DATETIME", updatable= false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_datetime", nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDateTime;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_blog", nullable = false)
    private Boolean isBlog;

    @JsonManagedReference
    @OneToMany(mappedBy = "wikiCategory", fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
    @OrderBy("createdDateTime")
    private Set<Wiki> wikiSet;

    @JsonManagedReference
    @OneToMany(mappedBy = "wikiCategory", fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
    private Set<NavTreeNode> navTreeNodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getBlog() {
        return isBlog;
    }

    public void setBlog(Boolean blog) {
        isBlog = blog;
    }

    public Set<Wiki> getWikiSet() {
        return wikiSet;
    }

    public void setWikiSet(Set<Wiki> wikiSet) {
        this.wikiSet = wikiSet;
    }

    public Set<NavTreeNode> getNavTreeNodes() {
        return navTreeNodes;
    }

    public void setNavTreeNodes(Set<NavTreeNode> navTreeNodes) {
        this.navTreeNodes = navTreeNodes;
    }
}
