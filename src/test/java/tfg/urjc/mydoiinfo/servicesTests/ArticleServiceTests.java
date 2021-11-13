package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.domain.entities.JCRRegistry;
import tfg.urjc.mydoiinfo.domain.entities.Journal;
import tfg.urjc.mydoiinfo.domain.repositories.ArticleRepository;
import tfg.urjc.mydoiinfo.domain.repositories.JCRRegistryRepository;
import tfg.urjc.mydoiinfo.domain.repositories.JournalRepository;
import tfg.urjc.mydoiinfo.services.ArticleService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
public class ArticleServiceTests {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    JournalRepository journalRepository;

    @Autowired
    JCRRegistryRepository jcrRegistryRepository;

    @Test
    public void getArticleFromNullDOITest(){
        //GIVEN: A null doi
        String nullDOI = null;

        //WHEN: The getArticleFromDOI is called with the null doi
        Article output = articleService.getArticleFromDOI(nullDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromMalformedDOITest(){
        //GIVEN: A fake doi
        String malformedDOI = "malformed";

        //WHEN: The getArticleFromDOI is called with the malformed doi
        Article output = articleService.getArticleFromDOI(malformedDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromErroneousDOITest(){
        //GIVEN: A erroneous doi
        String erroneousDOI = "http://www.erroneousDOI.com";

        //WHEN: The getArticleFromDOI is called with the erroneous doi
        Article output = articleService.getArticleFromDOI(erroneousDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromDOIWithStatusCodeDistinctTo200Test(){
        //GIVEN: A doi that returns 404 Not Found error
        String doi404 = "http://www.google.com/erroneousDOI";

        //WHEN: The getArticleFromDOI is called with the 404 error doi
        Article output = articleService.getArticleFromDOI(doi404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromCorrectDOITest(){
        //GIVEN: A correct doi
        String correctDOI = "https://doi.org/10.1126/science.370.6523.1384";

        //WHEN: The getArticleFromDOI is called with the correct doi
        Article output = articleService.getArticleFromDOI(correctDOI);

        //THEN: The output must be distinct from null
        assertNotNull(output);
        //AND: The output must be the article with the correct doi stored in the database
        assertEquals(articleRepository.findFirstByDOI(correctDOI),output);
    }

    @Test
    public void getArticleFromCorrectDOIWithCorrectJCRRegistryTest(){
        //GIVEN: A correct doi
        String correctDOI = "https://doi.org/10.1126/science.370.6523.1384";
        //AND: The Journal related to the article with the correctDOI
        Journal testJournal = new Journal("Science","Science");
        testJournal = journalRepository.save(testJournal);
        //AND: The JCRRegistry related to the article with the correctDOI
        JCRRegistry testJCR = new JCRRegistry(2020,testJournal,1.2F,1.2F,"Q1");
        testJCR = jcrRegistryRepository.save(testJCR);
        testJournal.getJcrRegistries().add(testJCR);
        journalRepository.save(testJournal);

        //WHEN: The getArticleFromDOI is called with the correct doi
        Article output = articleService.getArticleFromDOI(correctDOI);

        //THEN: The output must be distinct from null
        assertNotNull(output);
        //AND: Output's JCRRegistry must be distinct from null
        assertNotNull(output.getJcrRegistry());
        //AND: Output's JCRRegistry must be the JCRRegistry of the year and journal of the article with the correct
        //doi stored in the database
        assertEquals(testJCR,output.getJcrRegistry());
    }

    @Test
    public void getArticleFromCorrectDOIWithCorrectJournalTest(){
        //GIVEN: A correct doi
        String correctDOI = "https://doi.org/10.1126/science.370.6523.1384";
        //AND: The Journal related to the article with the correctDOI
        Journal testJournal = new Journal("Science","Science");
        testJournal = journalRepository.save(testJournal);
        //AND: The JCRRegistry related to the article with the correctDOI
        JCRRegistry testJCR = new JCRRegistry(2020,testJournal,1.2F,1.2F,"Q1");
        testJCR = jcrRegistryRepository.save(testJCR);
        testJournal.getJcrRegistries().add(testJCR);
        testJournal = journalRepository.save(testJournal);

        //WHEN: The getArticleFromDOI is called with the correct doi
        Article output = articleService.getArticleFromDOI(correctDOI);

        //THEN: The output must be distinct from null
        assertNotNull(output);
        //AND: Output's Journal must be distinct from null
        assertNotNull(output.getJcrRegistry().getJournal());
        //AND: Output's Journal must be the Journal of the article with the correct doi stored in the database
        assertEquals(testJournal,output.getJcrRegistry().getJournal());
    }
}
