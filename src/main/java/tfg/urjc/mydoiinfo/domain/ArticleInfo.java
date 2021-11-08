package tfg.urjc.mydoiinfo.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class ArticleInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String title;
    private String DOI;
    @ElementCollection(targetClass=String.class,fetch=FetchType.EAGER)
    private List<String> authors;
    private String journal;
    private String volumeInfo;
    private Date publicationDate;
    private String publicationDateText;
    Integer publicationYear;

    public ArticleInfo() {

    }

    public ArticleInfo(String title, String DOI, List<String> authors, String journal, String volumeInfo, Date publicationDate, String publicationDateText) {
        this.title = title;
        this.DOI = DOI;
        this.authors = authors;
        this.journal = journal;
        this.volumeInfo = volumeInfo;
        this.publicationDate = publicationDate;
        this.publicationDateText = publicationDateText;
        try{
            this.publicationYear = Integer.parseInt(publicationDateText.substring(publicationDateText.length()-4));
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
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

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
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
        try{
            this.publicationYear = Integer.parseInt(publicationDateText.substring(publicationDateText.length()-4));
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        String out = "ArticleInfo{";
        if (title != null)
            out = out + "title='" + title + '\'';
        if(DOI != null)
            out = out + ", DOI='" + DOI + "\'";
        if (authors != null)
            out = out + ", authors=" + authors;
        if (journal != null)
            out = out + ", journal='" + journal + '\'';
        if (volumeInfo != null)
            out = out + ", volumeInfo='" + volumeInfo + '\'';
        if (publicationDateText != null)
            out = out + ", publicationDate=" + publicationDateText;
        if (publicationYear != null)
            out = out + ", publicationYear=" + publicationYear;
        return out + '}';
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleInfo)) return false;
        ArticleInfo that = (ArticleInfo) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDOI(), that.getDOI()) && Objects.equals(getAuthors(), that.getAuthors()) && Objects.equals(getJournal(), that.getJournal()) && Objects.equals(getVolumeInfo(), that.getVolumeInfo()) && Objects.equals(getPublicationDate(), that.getPublicationDate()) && Objects.equals(getPublicationDateText(), that.getPublicationDateText()) && Objects.equals(getPublicationYear(), that.getPublicationYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDOI(), getAuthors(), getJournal(), getVolumeInfo(), getPublicationDate(), getPublicationDateText(), getPublicationYear());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
