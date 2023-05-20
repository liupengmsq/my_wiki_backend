package pengliu.me.backend.demo.nav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import pengliu.me.backend.demo.wiki.WikiCategory;
import pengliu.me.backend.demo.wiki.WikiCategoryRepository;

@Service
@Transactional(readOnly = true)
public class NavTreeService {
    @Autowired
    private NavTreeRepository navTreeRepository;
    @Autowired
    private WikiCategoryRepository wikiCategoryRepository;

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

    public List<NavTreeNode> getTreeNodesByCategoryId(Long categoryId) {
        return navTreeRepository.findByWikiCategoryId(categoryId);
    }

    public List<NavTreeNode> getTreeNodesByCategoryIdAndTarget(Long categoryId, String target) {
        return navTreeRepository.findByWikiCategoryIdAndTarget(categoryId, target);
    }

    public NavTreeNode getTreeNodeById(Long id) {
        Optional<NavTreeNode> node = navTreeRepository.findById(id);
        Assert.isTrue(node.isPresent(), String.format("找不到节点 %s\n", id));
        return node.get();
    }

    public NavTreeNode getTreeRootNodeByCategoryId(Long categoryId) {
        List<NavTreeNode> root = navTreeRepository.findRootNodeByWikiCategoryId(categoryId);
        Assert.isTrue(root.size() > 0 , "找不到根节点");
        Assert.isTrue(root.size() == 1 , "存在多个根节点");
        return root.get(0);
    }

    @Transactional(readOnly = false)
    public NavTreeNode createNavTreeNode(Long parentId, NavTreeNode newNode, Long categoryId) {
        if (newNode.getRoot()) {
            newNode.setDepth(0);
            Assert.isTrue(!existRootNodeInNavTree(categoryId), "已经存在根节点，不能再新建根节点了！！");
        } else {
            Optional<NavTreeNode> parent = navTreeRepository.findById(parentId);
            Assert.isTrue(parent.isPresent(),
                    String.format("找不到新节点对应的父节点，\n" +
                            "新节点为： %s \n", newNode.toString()));
            newNode.setParent(parent.get());
            newNode.setDepth(parent.get().getDepth() + 1);
        }
        Optional<WikiCategory> category = wikiCategoryRepository.findById(categoryId);
        Assert.isTrue(category.isPresent(), "指定的 category id 不存在！！");
        newNode.setWikiCategory(category.get());
        return navTreeRepository.save(newNode);
    }

    @Transactional(readOnly = false)
    public Boolean existRootNodeInNavTree(Long categoryId) {
        return navTreeRepository.findRootNodeByWikiCategoryId(categoryId).size() > 0;
    }

    @Transactional(readOnly = false)
    public void deleteNavTreeNode(Long id) throws Exception {
        Optional<NavTreeNode> node = navTreeRepository.findById(id);
        Assert.isTrue(node.isPresent(), String.format("待删除的节点不存在，其ID为： %s", id));

        if (node.get().getChildNodes().size() > 0 ) {
            throw new Exception("不能删除一个非叶子节点！！");
        }

        navTreeRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void updateNavTreeNode(NavTreeNode nodeToUpdate) {
        Optional<NavTreeNode> node = navTreeRepository.findById(nodeToUpdate.getId());
        Assert.isTrue(node.isPresent(), String.format("待更新节点不存在，其ID为：%s", nodeToUpdate.getId()));

        node.get().setTarget(nodeToUpdate.getTarget());
        node.get().setTitle(nodeToUpdate.getTitle());
        navTreeRepository.save(node.get());
    }
}
