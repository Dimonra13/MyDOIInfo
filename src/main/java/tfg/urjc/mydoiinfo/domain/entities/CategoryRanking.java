package tfg.urjc.mydoiinfo.domain.entities;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CategoryRanking {

    public interface CategoryRankingData {};

    @Id
    @GeneratedValue
    @JsonView(CategoryRankingData.class)
    private Long id;
    @JsonView(CategoryRankingData.class)
    private String name;
    @JsonView(CategoryRankingData.class)
    private String ranking;
    @JsonView(CategoryRankingData.class)
    private String journalField;

    public CategoryRanking(){

    }

    public CategoryRanking(String name, String ranking, String journalField) {
        this.name = name;
        this.ranking = ranking;
        setJournalField(journalField);
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

    public String getJournalField() {
        return journalField;
    }

    public void setJournalField(String journalField) {
        //JournalField can only be Science, Social Sciences or null
        this.journalField = (journalField.equals("Sciences") || journalField.equals("Social Sciences")) ? journalField : this.journalField;
    }

    @Override
    public String toString() {
        String out = "CategoryRanking{";
        if (name != null)
            out = out + "name='" + name + '\'';
        if(ranking != null)
            out = out + ", ranking='" + ranking + '\'';
        if (journalField != null)
            out = out + ", journalField='" + journalField + '\'';
        return out + '}';
    }
}
