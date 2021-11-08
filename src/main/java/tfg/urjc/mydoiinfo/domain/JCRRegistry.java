package tfg.urjc.mydoiinfo.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class JCRRegistry {
    @Id
    @GeneratedValue
    private Long id;
    private Integer year;
    @ManyToOne
    private Journal journal;
    private float jcr;
    private float jcrFiveYear;
    @OneToMany
    @JoinTable(
            inverseJoinColumns = @JoinColumn(
                    name = "CATEGORY_RANKING_ID",
                    referencedColumnName = "ID"
            )
    )
    private List<CategoryRanking> categoryRankingList;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
