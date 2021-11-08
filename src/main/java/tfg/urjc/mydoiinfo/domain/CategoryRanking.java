package tfg.urjc.mydoiinfo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CategoryRanking {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String ranking;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
