package pengliu.me.backend.demo.wiki;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pengliu.me.backend.demo.ResponseDocument;
import pengliu.me.backend.demo.WikiConfiguration;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WikiController {
    @Autowired
    private WikiConfiguration wikiConfiguration;

    @Autowired
    private WikiService wikiService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/wiki/category")
    public ResponseDocument<List<WikiCategory>> getAllWikiCategories() {
        return ResponseDocument.successResponse(wikiService.getAllWikiCategories());
    }

    @GetMapping("/wiki/category/default")
    public ResponseDocument<WikiCategory> getDefaultWikiCategory() {
        return ResponseDocument.successResponse(wikiService.getDefaultWikiCategory());
    }

    @PostMapping("/wiki/category")
    public ResponseDocument<WikiCategory> createWikiCategory(@RequestBody WikiCategory wikiCategory) {
        return ResponseDocument.successResponse(wikiService.createUpdateWikiCategory(wikiCategory));
    }

    @PutMapping("/wiki/category")
    public ResponseDocument<WikiCategory> updateWikiCategory(@RequestBody WikiCategory wikiCategory) {
        return ResponseDocument.successResponse(wikiService.createUpdateWikiCategory(wikiCategory));
    }

    @DeleteMapping("/wiki/category/{id}")
    public ResponseDocument<?> deleteWikiCategoryById(@PathVariable Long id) {
        wikiService.deleteWikiCategoryById(id);
        return ResponseDocument.emptySuccessResponse();
    }

    @GetMapping("/wiki")
    public ResponseDocument<List<WikiDTO>> getAllWikiPages() {
        List<Wiki> wikiList = wikiService.getAllWikiPages();
        return ResponseDocument.successResponse(wikiList.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/wiki/pageable")
    public ResponseDocument<List<WikiDTO>> getPageableAllWikiPages(@RequestParam Integer pageIndex, @RequestParam Integer size) {
        Page<Wiki> wikiList = wikiService.getAllWikiPagesOrderByCreatedDateTimeDesc(PageRequest.of(pageIndex, size));
        return ResponseDocument.successResponse(wikiList.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/wiki/search")
    public ResponseDocument<List<WikiDTO>> searchWikiPages(@RequestParam("searchText") String searchText) {
        List<Wiki> wikiList = wikiService.searchWikiPage(searchText);
        return ResponseDocument.successResponse(wikiList.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @GetMapping("/wiki/{id}")
    public ResponseDocument<WikiDTO> getWikiPageById(@PathVariable Long id, @RequestParam(value="updateAccessInfo", defaultValue = "false") Boolean updateAccessInfo) {
        return ResponseDocument.successResponse(convertToDto(wikiService.getWikiById(id, updateAccessInfo)));
    }

    @DeleteMapping("/wiki/{id}")
    public ResponseDocument<?> deleteWikiPageById(@PathVariable Long id) {
        wikiService.deleteWikiById(id);
        return ResponseDocument.emptySuccessResponse();
    }

    @PostMapping("/wiki")
    public ResponseDocument<WikiDTO> createWikiPage(@RequestBody WikiDTO wikiDTO) {
        return createUpdateWikiPage(wikiDTO);
    }

    @PutMapping("/wiki")
    public ResponseDocument<WikiDTO> updateWikiPage(@RequestBody WikiDTO wikiDTO) {
        return createUpdateWikiPage(wikiDTO);
    }

    private ResponseDocument<WikiDTO> createUpdateWikiPage(@RequestBody WikiDTO wikiDTO) {
        Wiki wiki = convertToEntity(wikiDTO);
        WikiCategory wikiCategory = wikiService.getWikiCategoryByName(wikiDTO.getCategoryName());
        wiki.setWikiCategory(wikiCategory);
        Wiki newCreatedWiki = wikiService.createUpdateWikiPage(wiki);
        return ResponseDocument.successResponse(convertToDto(newCreatedWiki));
    }

    @GetMapping("/wiki/image")
    public ResponseDocument<List<WikiImage>> getAllImages(@RequestParam(defaultValue = "") String fileName) {
        return ResponseDocument.successResponse(wikiService.getAllWikiImagesByFileName(fileName));
    }


    @PostMapping("/wiki/image")
    public ResponseDocument<?> uploadImage(MultipartFile file) throws Exception {
        return ResponseDocument.successResponse(wikiService.uploadWikiImage(file));
    }

    @DeleteMapping("/wiki/image/{fileName}")
    public ResponseDocument<?> deleteImage(@PathVariable  String fileName) throws Exception {
        wikiService.deleteWikiImage(fileName);
        return ResponseDocument.emptySuccessResponse();
    }

    private WikiDTO convertToDto(Wiki wiki) {
        modelMapper.typeMap(Wiki.class, WikiDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getWikiCategory().getId(), WikiDTO::setCategoryId);
                    mapper.map(src -> src.getWikiCategory().getCategoryName(), WikiDTO::setCategoryName);
                });
        return modelMapper.map(wiki, WikiDTO.class);
    }

    private Wiki convertToEntity(WikiDTO wikiDto) {
        return modelMapper.map(wikiDto, Wiki.class);
    }
}
