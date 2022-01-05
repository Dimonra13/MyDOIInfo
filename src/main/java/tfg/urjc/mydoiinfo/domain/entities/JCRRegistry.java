package tfg.urjc.mydoiinfo.domain.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class JCRRegistry {

    public interface JCRBasicData {};
    public interface JCRRelatedData {};

    @Id
    @GeneratedValue
    private Long id;
    @JsonView(JCRBasicData.class)
    private Integer year;
    @ManyToOne
    @JsonView(JCRRelatedData.class)
    private Journal journal;
    @JsonView(JCRBasicData.class)
    private Float impactFactor;
    @JsonView(JCRBasicData.class)
    private Float impactFactorFiveYear;
    @JsonView(JCRBasicData.class)
    private String quartile;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            inverseJoinColumns = @JoinColumn(
                    name = "CATEGORY_RANKING_ID",
                    referencedColumnName = "ID"
            )
    )
    @JsonView(JCRRelatedData.class)
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

    public Float getImpactFactor() {
        return impactFactor;
    }

    public void setImpactFactor(float impactFactor) {
        this.impactFactor = impactFactor;
    }

    public Float getImpactFactorFiveYear() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JCRRegistry)) return false;
        JCRRegistry that = (JCRRegistry) o;
        return Objects.equals(getYear(), that.getYear()) && Objects.equals(getJournal(), that.getJournal()) && Objects.equals(getImpactFactor(), that.getImpactFactor()) && Objects.equals(getImpactFactorFiveYear(), that.getImpactFactorFiveYear()) && Objects.equals(getQuartile(), that.getQuartile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getJournal(), getImpactFactor(), getImpactFactorFiveYear(), getQuartile());
    }

    @Override
    public String toString() {
        String out = "JCRRegistry{";
        if (year != null)
            out = out + "year=" + year;
        if(journal != null)
            out = out + ", journal=" + journal.toString();
        if (impactFactor != null)
            out = out + ", impactFactor=" + impactFactor;
        if (impactFactorFiveYear != null)
            out = out + ", impactFactorFiveYear=" + impactFactorFiveYear;
        if(quartile != null)
            out = out + ", quartile='" + quartile + '\'';
        if (categoryRankingList != null)
            out = out + ", categoryRankingList=" + categoryRankingList.toString();
        return out + '}';
    }
}
