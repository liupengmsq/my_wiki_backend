package pengliu.me.backend.demo.nav;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "nav_node")
public class NavTreeNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_root", nullable = false)
    private Boolean isRoot;

    @JsonBackReference
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public Integer getParentId() {
        if (parent == null) {
            return -1;
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
