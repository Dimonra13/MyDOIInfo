package tfg.urjc.mydoiinfo.domain.entities;


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
    @ElementCollection(targetClass=String.class)
    private List<String> authors;
    @ManyToOne
    private JCRRegistry jcrRegistry;
    private String volumeInfo;
    private Date publicationDate;
    private String publicationDateText;

    public Article() {
    }

    public Article(String title, String DOI, List<String> authors, JCRRegistry jcrRegistry, String volumeInfo, Date publicationDate, String publicationDateText) {
        this.title = title;
        this.DOI = DOI;
        this.authors = authors;
        this.jcrRegistry = jcrRegistry;
        this.volumeInfo = volumeInfo;
        this.publicationDate = publicationDate;
        this.publicationDateText = publicationDateText;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public JCRRegistry getJcrRegistry() {
        return jcrRegistry;
    }

    public void setJcrRegistry(JCRRegistry jcrRegistry) {
        this.jcrRegistry = jcrRegistry;
    }

    public String getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(String volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublicationDateText() {
        return publicationDateText;
    }

    public void setPublicationDateText(String publicationDateText) {
        this.publicationDateText = publicationDateText;
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
