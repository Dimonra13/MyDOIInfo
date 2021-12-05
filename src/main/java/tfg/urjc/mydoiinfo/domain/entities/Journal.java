package tfg.urjc.mydoiinfo.domain.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Journal {

    public interface JournalBasicData {};
    public interface JournalRelatedData {};

    @Id
    @GeneratedValue
    @JsonView(JournalBasicData.class)
    private Long id;
    @JsonView(JournalBasicData.class)
    private String title;
    @JsonView(JournalBasicData.class)
    private String shortTitle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "journal",fetch = FetchType.EAGER)
    @JsonView(JournalRelatedData.class)
    private List<JCRRegistry> jcrRegistries;

    public Journal() {
        this.jcrRegistries = new ArrayList<>();
    }

    public Journal(String title,String shortTitle) {
        this();
        this.title = title;
        this.shortTitle = shortTitle;
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

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public List<JCRRegistry> getJcrRegistries() {
        return jcrRegistries;
    }

    public void setJcrRegistries(List<JCRRegistry> jcrRegistries) {
        this.jcrRegistries = jcrRegistries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Journal)) return false;
        Journal journal = (Journal) o;
        return Objects.equals(getTitle(), journal.getTitle()) && Objects.equals(getShortTitle(), journal.getShortTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getShortTitle());
    }

    @Override
    public String toString() {
        String out = "Journal{";
        if (title != null)
            out = out + "title='" + title + '\'';
        if(shortTitle != null)
            out = out + ", shortTitle='" + shortTitle + '\'';
        return out + '}';
    }
}
