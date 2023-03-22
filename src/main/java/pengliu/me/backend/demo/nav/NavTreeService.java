package pengliu.me.backend.demo.nav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class NavTreeService {
    @Autowired
    private NavTreeRepository navTreeRepository;

    private void setDepthForNavTree(NavTreeNode root) {
        setDepthForNavTreeNode(root, root.getDepth());
    }

    private void setDepthForNavTreeNode(NavTreeNode current, Integer depth) {
        if (current == null) {
            return;
        }
        current.setDepth(depth);
        for (NavTreeNode child: current.getChildNodes()) {
            setDepthForNavTreeNode(child, depth + 1);
        }
    }

    public List<NavTreeNode> getAllTreeNodes() {
        return navTreeRepository.findAll();
    }

    public void createNavTreeNode(Integer parentId, NavTreeNode newNode) {
        Optional<NavTreeNode> parent = navTreeRepository.findById(parentId);
        Assert.isTrue(parent.isPresent(),
                String.format("Fail to find parent node of newNode.\n" +
                        "NewNode is: %s \n", newNode.toString()));
        newNode.setParent(parent.get());
        newNode.setDepth(parent.get().getDepth() + 1);
        navTreeRepository.save(newNode);
    }

    public void deleteNavTreeNode(Integer id) throws Exception {
        Optional<NavTreeNode> node = navTreeRepository.findById(id);
        Assert.isTrue(node.isPresent(), String.format("Fail to find node with id %s", id));

        if (node.get().getChildNodes().size() > 0 ) {
            throw new Exception("Fail to delete node when node is not a leaf node!!!");
        }

        navTreeRepository.deleteById(id);
    }

    public void updateNavTreeNode(NavTreeNode nodeToUpdate) {
        Optional<NavTreeNode> node = navTreeRepository.findById(nodeToUpdate.getId());
        Assert.isTrue(node.isPresent(), String.format("Fail to find node with id %s", nodeToUpdate.getId()));

        node.get().setTarget(nodeToUpdate.getTarget());
        node.get().setTitle(nodeToUpdate.getTitle());
        navTreeRepository.save(node.get());
    }
}
