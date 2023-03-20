package pengliu.me.backend.demo.controller;

import org.springframework.web.bind.annotation.*;
import pengliu.me.backend.demo.Node;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class NavTreeController {
    @GetMapping("/nav")
    public List<Node> getNavTree() {
        Node root = new Node(10, 0, true, "这是根节点");
        Node node1 = new Node(11, "这是节点1", root);
        Node node2 = new Node(12, "这是节点2", root);
        Node node3 = new Node(13, "这是节点3", root);
        root.addToNode(node1);
        root.addToNode(node2);
        root.addToNode(node3);

        Node node4 = new Node(999, "这是节点4", node1);
        node1.addToNode(node4);

        Node node5 = new Node(51, "这是节点5", node3);
        Node node6 = new Node(16, "这是节点6", node3);
        node3.addToNode(node5);
        node3.addToNode(node6);

        return Node.getNodesByDepth(root);
    }
}
