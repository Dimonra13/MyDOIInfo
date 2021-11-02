package tfg.urjc.mydoiinfo.domain;

import java.util.Date;
import java.util.List;

public class ArticleInfo {
    private String title;
    private String DOI;
    private List<String> authors;
    private String journal;
    private String volumeInfo;
    private Date publicationDate;
    private String publicationDateText;

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
    }

    @Override
    public String toString() {
        String out = "ArticleInfo{";
        if (title != null)
            out = out + "title='" + title + '\'';
        if(DOI != null)
            out = out + "DOI='" + DOI + "\'";
        if (authors != null)
            out = out + ", authors=" + authors;
        if (journal != null)
            out = out + ", journal='" + journal + '\'';
        if (volumeInfo != null)
            out = out + ", volumeInfo='" + volumeInfo + '\'';
        if (publicationDateText != null)
            out = out + ", publicationDate=" + publicationDateText;
        return out + '}';
    }


}
