package tfg.urjc.mydoiinfo.domain;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Article {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Column(nullable = false)
    private String DOI;
    @ElementCollection(targetClass=String.class,fetch=FetchType.EAGER)
    private List<String> authors;
    @ManyToOne
    private JCRRegistry jcrRegistry;
    private String volumeInfo;
    private Date publicationDate;
    private String publicationDateText;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(DOI, article.DOI);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DOI);
    }
}
