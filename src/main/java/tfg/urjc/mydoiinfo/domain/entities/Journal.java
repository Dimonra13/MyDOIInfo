package tfg.urjc.mydoiinfo.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Journal {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String shortTitle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "journal",fetch = FetchType.EAGER)
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
}
