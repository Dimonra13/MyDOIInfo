package tfg.urjc.mydoiinfo.scrappers;

import java.util.*;

public class ArticleInfo {

    private String title;
    private String DOI;
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

    public String getDOI() {
        return DOI;
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

    public String getVolumeInfo() {
        return volumeInfo;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public String getPublicationDateText() {
        return publicationDateText;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Integer getPublicationDateYear() {
        if(publicationDate==null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.publicationDate);
        return calendar.get(Calendar.YEAR);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleInfo)) return false;
        ArticleInfo that = (ArticleInfo) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDOI(), that.getDOI()) && Objects.equals(getJournal(), that.getJournal()) && Objects.equals(getVolumeInfo(), that.getVolumeInfo()) && Objects.equals(getPublicationDate(), that.getPublicationDate()) && Objects.equals(getPublicationDateText(), that.getPublicationDateText()) && Objects.equals(getPublicationYear(), that.getPublicationYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDOI(), getJournal(), getVolumeInfo(), getPublicationDate(), getPublicationDateText(), getPublicationYear());
    }
}
