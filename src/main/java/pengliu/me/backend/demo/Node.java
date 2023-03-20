package pengliu.me.backend.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Integer id;
    private Integer depth;
    private String content;
    private Boolean isRoot = false;
    @JsonIgnore
    private Node parent;
    private Integer parentId = -1;
    @JsonIgnore
    private List<Node> childNodes;

    private static List<Node> NODES_BY_DEPTH = new ArrayList<>();

    public static List<Node> getNodesByDepth(Node root) {
        NODES_BY_DEPTH.clear();
        printAllFromRoot(root);
        return NODES_BY_DEPTH;
    }

    public static void printAllFromRoot(Node root) {
        print(root, root.getDepth());
    }

    private  static void print(Node current, Integer depth) {
        if (current == null) {
            return;
        }
        current.setDepth(depth);

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<depth; i++) {
            sb.append("----");
        }
        System.out.println(String.format(sb.toString() + "%d(%d)", current.getId(), depth));
        NODES_BY_DEPTH.add(current);
        for (Node child: current.childNodes) {
            print(child, depth + 1);
        }
    }

    public Node() {
        childNodes = new ArrayList<>();
    }

    public Node(Integer id, String content) {
        this();
        this.id = id;
        this.content = content;
    }

    public Node(Integer id, String content, Node parent) {
        this(id, content);
        this.parent = parent;
        this.parentId = parent.getId();
    }

    public Node(Integer id, Integer depth, Boolean isRoot, String content) {
        this();
        this.id = id;
        this.depth = depth;
        this.isRoot = isRoot;
        this.content = content;
    }

    public void addToNode(Node node) {
        this.childNodes.add(node);
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    public Integer getDepth() {
        return depth;
    }

    private void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    private void setRoot(Boolean root) {
        isRoot = root;
    }

    public Node getParent() {
        return parent;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + ", depth=" + depth + ", parent=" + (parent == null ? "" : parentId) + "}";
    }
}
