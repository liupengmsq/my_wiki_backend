package pengliu.me.backend.demo.wiki;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import pengliu.me.backend.demo.util.DateTimeUtil;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WikiService {
    @Autowired
    private WikiRepository wikiRepository;

    @Autowired
    private WikiCategoryRepository wikiCategoryRepository;

    public List<WikiCategory> getAllWikiCategories() {
        return wikiCategoryRepository.findAll();
    }

    public List<Wiki> getAllWikiPages() {
        return wikiRepository.findAll();
    }

    public Wiki getWikiById(Long id) {
        Optional<Wiki> wiki = wikiRepository.findById(id);
        Assert.isTrue(wiki.isPresent(), "不存在对应的wiki id");

        return wiki.get();
    }

    public WikiCategory getWikiCategoryById(Long id) {
        Optional<WikiCategory> category = wikiCategoryRepository.findById(id);
        Assert.isTrue(category.isPresent(), "不存在对应的wiki id");

        return category.get();
    }

    @Transactional(readOnly = false)
    public void createUpdateWikiPage(Wiki wiki) {
        wikiRepository.save(wiki);
    }
}
