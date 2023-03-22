package pengliu.me.backend.demo.nav;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class NavTreeController {
    @Autowired
    private NavTreeService navTreeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/nav/tree")
    public List<NavTreeNodeDTO> getNavTreeNodes() {
        List<NavTreeNode> navTreeNodes = navTreeService.getAllTreeNodes();
        return navTreeNodes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @PostMapping("/nav/tree")
    public void createNavTreeNode(@RequestBody NavTreeNodeDTO node) {
        NavTreeNode navTreeNode = convertToEntity(node);
        navTreeService.createNavTreeNode(node.getParentId(), navTreeNode);
    }

    @DeleteMapping("/nav/tree/{id}")
    public void deleteNavTreeNode(@PathVariable Integer id) throws Exception {
        navTreeService.deleteNavTreeNode(id);
    }

    @PutMapping("/nav/tree/{id}")
    public void updateNavTreeNode(@PathVariable Integer id, @RequestBody NavTreeNodeDTO nodeToUpdate) {
        nodeToUpdate.setId(id);
        NavTreeNode navTreeNode = convertToEntity(nodeToUpdate);
        navTreeService.updateNavTreeNode(navTreeNode);
    }

    private NavTreeNodeDTO convertToDto(NavTreeNode navTreeNode) {
        modelMapper.typeMap(NavTreeNode.class, NavTreeNodeDTO.class).addMappings(mapper -> {
            mapper.map(NavTreeNode::getParentId,
                    NavTreeNodeDTO::setParentId);
        });
        return modelMapper.map(navTreeNode, NavTreeNodeDTO.class);
    }

    private NavTreeNode convertToEntity(NavTreeNodeDTO navTreeNodeDto) {
        return modelMapper.map(navTreeNodeDto, NavTreeNode.class);
    }
}
