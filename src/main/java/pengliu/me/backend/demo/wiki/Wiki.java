package pengliu.me.backend.demo.wiki;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * CREATE TABLE `wiki` (
 *   `id` int NOT NULL AUTO_INCREMENT,
 *   `title` varchar(500) NOT NULL,
 *   `category_type_id` int NOT NULL,
 *   `markdown_content` longtext,
 *   `created_datetime` datetime NOT NULL,
 *   `updated_datetime` datetime NOT NULL,
 *   `access_datetime` datetime DEFAULT NULL,
 *   `page_viewed_number` int DEFAULT '0',
 *   PRIMARY KEY (`id`),
 *   KEY `category_type_id_idx` (`category_type_id`),
 *   CONSTRAINT `category_type_id` FOREIGN KEY (`category_type_id`) REFERENCES `wiki_category` (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 */
@Entity
@Table(name = "wiki")
public class Wiki {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "markdown_content", nullable = true)
    @Type(type="text")
    private String markdownContent;

    @CreationTimestamp
    @Column(name = "created_datetime", nullable = false, columnDefinition="DATETIME", updatable= false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDateTime;

    @UpdateTimestamp
    @Column(name = "updated_datetime", nullable = false, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDateTime;

    @Column(name = "access_datetime", nullable = true, columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accessDateTime;

    @Column(name = "page_viewed_number", nullable = true)
    private Integer pageViewedNumber = 0;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_type_id", referencedColumnName = "id", nullable = false)
    private WikiCategory wikiCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Date getAccessDateTime() {
        return accessDateTime;
    }

    public void setAccessDateTime(Date accessDateTime) {
        this.accessDateTime = accessDateTime;
    }

    public Integer getPageViewedNumber() {
        return pageViewedNumber;
    }

    public void setPageViewedNumber(Integer pageViewedNumber) {
        this.pageViewedNumber = pageViewedNumber;
    }

    public WikiCategory getWikiCategory() {
        return wikiCategory;
    }

    public void setWikiCategory(WikiCategory wikiCategory) {
        this.wikiCategory = wikiCategory;
    }
}
