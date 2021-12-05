package tfg.urjc.mydoiinfo.domain.entities;


import com.fasterxml.jackson.annotation.JsonView;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

import javax.persistence.*;
import java.util.*;

@Entity
public class Article {

    public interface ArticleBasicData {};
    public interface ArticleRelatedData {};


    @Id
    @GeneratedValue
    @JsonView(ArticleBasicData.class)
    private Long id;
    @JsonView(ArticleBasicData.class)
    private String title;
    @Column(nullable = false)
    @JsonView(ArticleBasicData.class)
    private String DOI;
    @ElementCollection(targetClass=String.class)
    @JsonView(ArticleRelatedData.class)
    private List<String> authors;
    @ManyToOne
    @JsonView(ArticleRelatedData.class)
    private JCRRegistry jcrRegistry;
    @JsonView(ArticleBasicData.class)
    private String journalTitle;
    @JsonView(ArticleBasicData.class)
    private String volumeInfo;
    @JsonView(ArticleBasicData.class)
    private Long citations;
    private Date publicationDate;
    @JsonView(ArticleBasicData.class)
    private String publicationDateText;
    private String conferenceAcronym;
    @ManyToOne
    @JsonView(ArticleRelatedData.class)
    private Conference conference;

    public Article() {
        authors = new ArrayList<>();
    }

    public Article(String title, String DOI, List<String> authors, JCRRegistry jcrRegistry, String volumeInfo, Date publicationDate, String publicationDateText, String conferenceAcronym) {
        this.title = title;
        this.DOI = DOI;
        this.authors = (authors != null) ? authors : new ArrayList<>();
        this.jcrRegistry = jcrRegistry;
        this.volumeInfo = volumeInfo;
        this.publicationDate = publicationDate;
        this.publicationDateText = publicationDateText;
        this.conferenceAcronym = conferenceAcronym;
    }

    public Article(ArticleInfo articleInfo) {
        this.title = articleInfo.getTitle();
        this.DOI = articleInfo.getDOI();
        this.authors = (articleInfo.getAuthors() != null) ? articleInfo.getAuthors() : new ArrayList<>();
        this.volumeInfo = articleInfo.getVolumeInfo();
        this.publicationDate = articleInfo.getPublicationDate();
        this.publicationDateText = articleInfo.getPublicationDateText();
        this.journalTitle = articleInfo.getJournal();
        this.conferenceAcronym = articleInfo.getConferenceAcronym();
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

    public Integer getPublicationDateYear() {
        if(publicationDate==null){
            if (publicationDateText==null || publicationDateText.equals(""))
                return null;
            Integer publicationYear;
            try{
                publicationYear = Integer.parseInt(publicationDateText.substring(publicationDateText.length()-4));
            }catch (Exception e){
                return null;
            }
            return publicationYear;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.publicationDate);
        return calendar.get(Calendar.YEAR);
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

    public String getConferenceAcronym() {
        return conferenceAcronym;
    }

    public void setConferenceAcronym(String conferenceAcronym) {
        this.conferenceAcronym = conferenceAcronym;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
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
