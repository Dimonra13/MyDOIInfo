package tfg.urjc.mydoiinfo.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Journal {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "journal",fetch = FetchType.EAGER)
    private List<JCRRegistry> jcrRegistries;

    public Journal() {
        this.jcrRegistries = new ArrayList<>();
    }

    public Journal(String name) {
        this();
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JCRRegistry> getJcrRegistries() {
        return jcrRegistries;
    }

    public void setJcrRegistries(List<JCRRegistry> jcrRegistries) {
        this.jcrRegistries = jcrRegistries;
    }
}
