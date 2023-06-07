package pengliu.me.backend.demo.nav;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import pengliu.me.backend.demo.ResponseDocument;
import pengliu.me.backend.demo.error.WikiException;

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
         curl --location 'http://localhost:9091/api/nav/tree?categoryId=1'
     * @return
     */
    @GetMapping("/nav/tree")
    public ResponseDocument<List<NavTreeNodeDTO>> getNavTreeNodeByCategoryIdAndTarget(@RequestParam("categoryId") Long categoryId,
                                                                                      @RequestParam(name = "target", required = false) String target) {
        List<NavTreeNode> navTreeNodes = null;
        if (StringUtils.isEmpty(target)) {
            navTreeNodes = navTreeService.getTreeNodesByCategoryId(categoryId);
        } else {
            navTreeNodes = navTreeService.getTreeNodesByCategoryIdAndTarget(categoryId, target);
        }
        return ResponseDocument.successResponse(navTreeNodes.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/nav/root")
    public ResponseDocument<NavTreeNodeDTO> getNavTreeRootByCategoryId(@RequestParam("categoryId") Long categoryId) {
        NavTreeNode navTreeNode = navTreeService.getTreeRootNodeByCategoryId(categoryId);
        return ResponseDocument.successResponse(convertToDto(navTreeNode));
    }

    @GetMapping("/nav/tree/{id}")
    public ResponseDocument<NavTreeNodeDTO> getNavTreeNodeById(@PathVariable Long id) {
        NavTreeNode navTreeNode = navTreeService.getTreeNodeById(id);
        return ResponseDocument.successResponse(convertToDto(navTreeNode));
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDocument<NavTreeNodeDTO> createNavTreeNode(@RequestBody NavTreeNodeDTO node, @RequestParam("categoryId") Long categoryId) {
        Assert.hasText(node.getTarget(), "新建节点的目标URL不能为空！！");
        Assert.hasText(node.getTitle(), "新建节点的标题不能为空！！");
        NavTreeNode navTreeNode = convertToEntity(node);
        NavTreeNode createdNode = navTreeService.createNavTreeNode(node.getParentId(), navTreeNode, categoryId);
        return ResponseDocument.successResponse(convertToDto(createdNode));
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDocument<?> deleteNavTreeNode(@PathVariable Long id) throws WikiException {
        navTreeService.deleteNavTreeNode(id);
        return ResponseDocument.emptySuccessResponse();
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDocument<?> updateNavTreeNode(@PathVariable Long id, @RequestBody NavTreeNodeDTO nodeToUpdate) {
        nodeToUpdate.setId(id);
        NavTreeNode navTreeNode = convertToEntity(nodeToUpdate);
        navTreeService.updateNavTreeNode(navTreeNode);
        return ResponseDocument.emptySuccessResponse();
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
