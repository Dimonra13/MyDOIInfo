package tfg.urjc.mydoiinfo.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class JCRRegistry {
    @Id
    @GeneratedValue
    private Long id;
    private Integer year;
    @ManyToOne
    private Journal journal;
    private Float impactFactor;
    private Float impactFactorFiveYear;
    private String quartile;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            inverseJoinColumns = @JoinColumn(
                    name = "CATEGORY_RANKING_ID",
                    referencedColumnName = "ID"
            )
    )
    private List<CategoryRanking> categoryRankingList;

    public JCRRegistry() {
        categoryRankingList = new ArrayList<>();
    }

    public JCRRegistry(Integer year, Journal journal, Float impactFactor, Float impactFactorFiveYear, String quartile) {
        this();
        this.year = year;
        this.journal = journal;
        this.impactFactor = impactFactor;
        this.impactFactorFiveYear = impactFactorFiveYear;
        this.quartile = quartile;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public float getImpactFactor() {
        return impactFactor;
    }

    public void setImpactFactor(float impactFactor) {
        this.impactFactor = impactFactor;
    }

    public float getImpactFactorFiveYear() {
        return impactFactorFiveYear;
    }

    public void setImpactFactorFiveYear(float impactFactorFiveYear) {
        this.impactFactorFiveYear = impactFactorFiveYear;
    }

    public String getQuartile() {
        return quartile;
    }

    public void setQuartile(String quartile) {
        this.quartile = quartile;
    }

    public List<CategoryRanking> getCategoryRankingList() {
        return categoryRankingList;
    }

    public void setCategoryRankingList(List<CategoryRanking> categoryRankingList) {
        this.categoryRankingList = categoryRankingList;
    }
}