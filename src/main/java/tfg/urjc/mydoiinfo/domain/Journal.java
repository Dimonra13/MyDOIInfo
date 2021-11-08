package tfg.urjc.mydoiinfo.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Journal {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "journal")
    private List<JCRRegistry> jcrRegistries;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
