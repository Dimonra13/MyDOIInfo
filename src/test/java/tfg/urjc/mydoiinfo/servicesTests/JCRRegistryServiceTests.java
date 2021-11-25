package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.domain.entities.JCRRegistry;
import tfg.urjc.mydoiinfo.domain.entities.Journal;
import tfg.urjc.mydoiinfo.domain.repositories.JCRRegistryRepository;
import tfg.urjc.mydoiinfo.domain.repositories.JournalRepository;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.services.JCRRegistryService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JCRRegistryServiceTests {

    @Autowired
    JCRRegistryService jcrRegistryService;

    @Autowired
    JCRRegistryRepository jcrRegistryRepository;

    @Autowired
    JournalRepository journalRepository;

    @BeforeAll
    void setUp(){
        Journal journal = new Journal("Science","Science");
        journal = journalRepository.save(journal);
        JCRRegistry jcrRegistry = new JCRRegistry(2020,journal,1.2F,1.2F,"Q1");
        jcrRegistry = jcrRegistryRepository.save(jcrRegistry);
        journal.getJcrRegistries().add(jcrRegistry);
        journalRepository.save(journal);
    }

    @Test
    public void setJCRRegistryFromArticleInfoNullArticleTest(){
        //GIVEN: A null article
        Article article = null;
        //AND: An ArticleInfo
        ArticleInfo articleInfo = new ArticleInfo();

        //WHEN: The setJCRRegistry method is called with the Article and ArticleInfo
        Article output = jcrRegistryService.setJCRRegistry(article,articleInfo);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void setJCRRegistryFromArticleInfoNullArticleInfoTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: A null ArticleInfo
        ArticleInfo articleInfo = null;

        //WHEN: The setJCRRegistry method is called with the Article and ArticleInfo
        Article output = jcrRegistryService.setJCRRegistry(article,articleInfo);

        //THEN: The output must be the original article
        assertEquals(article,output);
    }

    @Test
    public void setJCRRegistryFromArticleInfoIncorrectDateTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: An ArticleInfo with a correct journal and an incorrect date
        ArticleInfo articleInfo = new ArticleInfo(null,null,null,"Science",null,null,"1745");

        //WHEN: The setJCRRegistry method is called with the Article and ArticleInfo
        Article output = jcrRegistryService.setJCRRegistry(article,articleInfo);

        //THEN: The output's JCRRegistry must be null
        assertNull(output.getJcrRegistry());
    }

    @Test
    public void setJCRRegistryFromArticleInfoIncorrectJournalTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: An ArticleInfo with an incorrect journal and a correct date
        ArticleInfo articleInfo = new ArticleInfo(null,null,null,"Sciencedfrgrtd",null,null,"2020");

        //WHEN: The setJCRRegistry method is called with the Article and ArticleInfo
        Article output = jcrRegistryService.setJCRRegistry(article,articleInfo);

        //THEN: The output's JCRRegistry must be null
        assertNull(output.getJcrRegistry());
    }

    @Test
    public void setJCRRegistryFromArticleInfoCorrectDateAndJournalTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: An ArticleInfo with a correct journal and a correct date
        ArticleInfo articleInfo = new ArticleInfo(null,null,null,"Science",null,null,"2020");

        //WHEN: The setJCRRegistry method is called with the Article and ArticleInfo
        Article output = jcrRegistryService.setJCRRegistry(article,articleInfo);

        //THEN: The output's JCRRegistry must be the one stored in the database with the same journal and publicationDate
        assertEquals(jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(2020,"Science"),output.getJcrRegistry());
    }

    @Test
    public void setJCRRegistryFromYearAndPublicationNullArticleTest(){
        //GIVEN: A null article
        Article article = null;
        //AND: A year
        Integer year = 2020;
        //AND: A journal title
        String journalTitle = "Science";

        //WHEN: The setJCRRegistry method is called with the Article, journal and year
        Article output = jcrRegistryService.setJCRRegistry(article,journalTitle,year);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void setJCRRegistryFromYearAndPublicationNullYearTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: A null year
        Integer year = null;
        //AND: A journal title
        String journalTitle = "Science";

        //WHEN: The setJCRRegistry method is called with the Article, journal and year
        Article output = jcrRegistryService.setJCRRegistry(article,journalTitle,year);

        //THEN: The output must be the original article
        assertEquals(article,output);
    }

    @Test
    public void setJCRRegistryFromYearAndPublicationIncorrectDateTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: An incorrect year
        Integer year = 1745;
        //AND: A journal title
        String journalTitle = "Science";

        //WHEN: The setJCRRegistry method is called with the Article, journal and year
        Article output = jcrRegistryService.setJCRRegistry(article,journalTitle,year);

        //THEN: The output's JCRRegistry must be null
        assertNull(output.getJcrRegistry());
    }

    @Test
    public void setJCRRegistryFromYearAndPublicationIncorrectJournalTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: A year
        Integer year = 2020;
        //AND: A incorrect journal title
        String journalTitle = "Scienceghjyytfyg";

        //WHEN: The setJCRRegistry method is called with the Article, journal and year
        Article output = jcrRegistryService.setJCRRegistry(article,journalTitle,year);

        //THEN: The output's JCRRegistry must be null
        assertNull(output.getJcrRegistry());
    }

    @Test
    public void setJCRRegistryFromYearAndPublicationCorrectDateAndJournalTest(){
        //GIVEN: An article
        Article article = new Article();
        article.setDOI("https://doi.org/10.1126/science.abb3420");
        //AND: A correct year
        Integer year = 2020;
        //AND: A correct journal title
        String journalTitle = "Science";

        //WHEN: The setJCRRegistry method is called with the Article, journal and year
        Article output = jcrRegistryService.setJCRRegistry(article,journalTitle,year);

        //THEN: The output's JCRRegistry must be the one stored in the database with the same journal and publicationDate
        assertEquals(jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(2020,"Science"),output.getJcrRegistry());
    }
}
