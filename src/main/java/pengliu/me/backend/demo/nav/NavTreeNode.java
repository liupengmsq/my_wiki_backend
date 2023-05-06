package pengliu.me.backend.demo.nav;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import pengliu.me.backend.demo.wiki.WikiCategory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * CREATE TABLE `nav_node` (
 *   `id` int NOT NULL AUTO_INCREMENT,
 *   `title` varchar(45) DEFAULT NULL,
 *   `is_root` tinyint(1) NOT NULL,
 *   `parent_id` int DEFAULT NULL,
 *   `target` text,
 *   `depth` int NOT NULL,
 *   PRIMARY KEY (`id`),
 *   KEY `parent_id_idx` (`parent_id`),
 *   CONSTRAINT `parent_id` FOREIGN KEY (`parent_id`) REFERENCES `nav_node` (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=542 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
@Entity
@Table(name = "nav_node")
public class NavTreeNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_root", nullable = false)
    private Boolean isRoot;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="parent_id", nullable=true)
    private NavTreeNode parent;

    @Column(name= "target", nullable = true)
    private String target;

    @JsonProperty("content")
    @Column(name = "title", nullable = true, length = 45)
    private String title;

    @Column(name = "depth", nullable = false)
    private Integer depth;

    @JsonIgnore
    @OneToMany(mappedBy="parent")
    private Set<NavTreeNode> childNodes = new HashSet<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_type_id", referencedColumnName = "id", nullable = false)
    private WikiCategory wikiCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public Long getParentId() {
        if (parent == null) {
            return -1L;
        }
        return parent.getId();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Set<NavTreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(Set<NavTreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    public void addChildNode(NavTreeNode childNode) {
        this.childNodes.add(childNode);
    }

    public NavTreeNode getParent() {
        return parent;
    }

    public void setParent(NavTreeNode parent) {
        this.parent = parent;
    }

    public WikiCategory getWikiCategory() {
        return wikiCategory;
    }

    public void setWikiCategory(WikiCategory wikiCategory) {
        this.wikiCategory = wikiCategory;
    }

    @Override
    public String toString() {
        return "NavTreeNode{" +
                "id=" + id +
                ", isRoot=" + isRoot +
                ", parentId=" + getParentId() +
                ", target='" + target + '\'' +
                ", title='" + title + '\'' +
                ", depth=" + depth +
                '}';
    }
}
