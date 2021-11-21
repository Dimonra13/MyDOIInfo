package tfg.urjc.mydoiinfo.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
public class Conference {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String acronym;
    private Integer ggsClass;
    private String ggsRating;
    private String coreClass;
    private Date updatedDate;

    public Conference() {
    }

    public Conference(String title, String acronym,Integer ggsClass, String ggsRating, String coreClass, Date updatedDate) {
        this.title = title;
        this.acronym = acronym;
        this.ggsClass = ggsClass;
        this.ggsRating = ggsRating;
        this.coreClass = coreClass;
        this.updatedDate = updatedDate;
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Integer getGgsClass() {
        return ggsClass;
    }

    public void setGgsClass(Integer ggsClass) {
        this.ggsClass = ggsClass;
    }

    public String getGgsRating() {
        return ggsRating;
    }

    public void setGgsRating(String ggsRating) {
        this.ggsRating = ggsRating;
    }

    public String getCoreClass() {
        return coreClass;
    }

    public void setCoreClass(String coreClass) {
        this.coreClass = coreClass;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conference)) return false;
        Conference that = (Conference) o;
        return Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getAcronym(), that.getAcronym()) && Objects.equals(getGgsClass(), that.getGgsClass()) && Objects.equals(getGgsRating(), that.getGgsRating()) && Objects.equals(getCoreClass(), that.getCoreClass()) && Objects.equals(getUpdatedDate(), that.getUpdatedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAcronym(), getGgsClass(), getGgsRating(), getCoreClass(), getUpdatedDate());
    }
}
