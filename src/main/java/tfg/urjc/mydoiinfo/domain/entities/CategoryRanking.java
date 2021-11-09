package tfg.urjc.mydoiinfo.domain.entities;

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

    public CategoryRanking(){

    }

    public CategoryRanking(String name, String ranking) {
        this.name = name;
        this.ranking = ranking;
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

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
