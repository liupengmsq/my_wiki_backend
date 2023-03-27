package pengliu.me.backend.demo.nav;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pengliu.me.backend.demo.ResponseDocument;

import java.util.ArrayList;
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


    /**
     * 获取树中所有节点的信息
     * HTTP请求：
         curl --location 'http://localhost:9091/api/nav/tree'
     * @return
     */
    @GetMapping("/nav/tree")
    public List<NavTreeNodeDTO> getNavTreeNodes() {
        List<NavTreeNode> navTreeNodes = navTreeService.getAllTreeNodes();
        return navTreeNodes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 创建新的节点
     * HTTP请求：
        - 创建根节点：
         curl --location 'http://localhost:9091/api/nav/tree' \
         --header 'Content-Type: application/json' \
         --data '{

         "target": "https://www.test.com",
         "root": true,
         "content": "根节点"
         }'

        - 创建子节点：
         curl --location 'http://localhost:9091/api/nav/tree' \
         --header 'Content-Type: application/json' \
         --data '{
         "parentId": 27,
         "target": "https://www.dsfsdfsf.com",
         "root": false,
         "content": "第二层节点3"
         }'
     * @param node
     */
    @PostMapping("/nav/tree")
    public NavTreeNodeDTO createNavTreeNode(@RequestBody NavTreeNodeDTO node) {
        NavTreeNode navTreeNode = convertToEntity(node);
        NavTreeNode createdNode = navTreeService.createNavTreeNode(node.getParentId(), navTreeNode);
        return convertToDto(createdNode);
    }

    /*
    curl --location --request DELETE 'http://localhost:9091/api/nav/tree/1'
     */

    /**
     * 删除节点 （被删除的节点必须是叶子节点，即没有子节点的节点才能被删除）
     *
     * HTTP请求：
         curl --location --request DELETE 'http://localhost:9091/api/nav/tree/1'
     * @param id
     * @throws Exception
     */
    @DeleteMapping("/nav/tree/{id}")
    public String deleteNavTreeNode(@PathVariable Integer id) throws Exception {
        navTreeService.deleteNavTreeNode(id);
        return "SUCCESS";
    }

    /**
     * 更新节点Id的target与content
     *
     * HTTP请求：
         curl --location --request PUT 'http://localhost:9091/api/nav/tree/2' \
         --header 'Content-Type: application/json' \
         --data '    {
         "target": "https://my/updated/target",
         "content": "新的节点标题"
         }'
     * @param id
     * @param nodeToUpdate
     */
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
