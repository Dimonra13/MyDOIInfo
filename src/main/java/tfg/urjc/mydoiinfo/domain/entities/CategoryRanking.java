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
        return "CategoryRanking{" +
                "name='" + name + '\'' +
                ", ranking='" + ranking + '\'' +
                ", journalField='" + journalField + '\'' +
                '}';
    }
}
