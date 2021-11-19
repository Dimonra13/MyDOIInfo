package tfg.urjc.mydoiinfo.domain.entities;


import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String journalTitle;
    private String volumeInfo;
    private Long citations;
    private Date publicationDate;
    private String publicationDateText;

    public Article() {
        authors = new ArrayList<>();
    }

    public Article(String title, String DOI, List<String> authors, JCRRegistry jcrRegistry, String volumeInfo, Date publicationDate, String publicationDateText) {
        this.title = title;
        this.DOI = DOI;
        this.authors = (authors != null) ? authors : new ArrayList<>();
        this.jcrRegistry = jcrRegistry;
        this.volumeInfo = volumeInfo;
        this.publicationDate = publicationDate;
        this.publicationDateText = publicationDateText;
    }

    public Article(ArticleInfo articleInfo) {
        this.title = articleInfo.getTitle();
        this.DOI = articleInfo.getDOI();
        this.authors = (articleInfo.getAuthors() != null) ? articleInfo.getAuthors() : new ArrayList<>();
        this.volumeInfo = articleInfo.getVolumeInfo();
        this.publicationDate = articleInfo.getPublicationDate();
        this.publicationDateText = articleInfo.getPublicationDateText();
        this.journalTitle = articleInfo.getJournal();
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

    public Long getCitations() {
        return citations;
    }

    public void setCitations(Long citations) {
        this.citations = citations;
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

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(getTitle(), article.getTitle()) && Objects.equals(getDOI(), article.getDOI()) && Objects.equals(getJournalTitle(), article.getJournalTitle()) && Objects.equals(getVolumeInfo(), article.getVolumeInfo()) && Objects.equals(getPublicationDate(), article.getPublicationDate()) && Objects.equals(getPublicationDateText(), article.getPublicationDateText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDOI(), getJournalTitle(), getVolumeInfo(), getPublicationDate(), getPublicationDateText());
    }

    @Override
    public String toString() {
        String out = "Article{";
        if (title != null)
            out = out + "title='" + title + '\'';
        if(DOI != null)
            out = out + ", DOI='" + DOI + "\'";
        if (authors != null)
            out = out + ", authors=" + authors.toString();
        if (jcrRegistry != null)
            out = out + ", jcrRegistry=" + jcrRegistry.toString();
        else if(journalTitle!=null)
            out = out + ", journal=" + journalTitle;
        if (volumeInfo != null)
            out = out + ", volumeInfo='" + volumeInfo + '\'';
        if(citations != null)
            out = out + ", citations=" + citations;
        if (publicationDateText != null)
            out = out + ", publicationDate=" + publicationDateText;
        return out + '}';
    }
}
