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

import java.util.Arrays;
import java.util.List;

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
    public void getArticleFromUserInputNullDOITest(){
        //GIVEN: A null doi
        String nullDOI = null;

        //WHEN: The getArticleFromUserInputDOI is called with the null doi
        Article output = articleService.getArticleFromUserInputDOI(nullDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromUserInputMalformedDOITest(){
        //GIVEN: A fake doi
        String malformedDOI = "malformed";

        //WHEN: The getArticleFromUserInputDOI is called with the malformed doi
        Article output = articleService.getArticleFromUserInputDOI(malformedDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromUserInputErroneousDOITest(){
        //GIVEN: A erroneous doi
        String erroneousDOI = "http://www.erroneousDOI.com";

        //WHEN: The getArticleFromUserInputDOI is called with the erroneous doi
        Article output = articleService.getArticleFromUserInputDOI(erroneousDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleFromUserInputCorrectDOIURLFormatTest(){
        //GIVEN: A correct doi (URL Format)
        String correctDOI = "https://doi.org/10.1126/science.370.6523.1384";

        //WHEN: The getArticleFromUserInputDOI is called with the correct doi
        Article output = articleService.getArticleFromUserInputDOI(correctDOI);

        //THEN: The output must be distinct from null
        assertNotNull(output);
        //AND: The output must be the article with the correct doi stored in the database
        assertEquals(articleRepository.findFirstByDOI(correctDOI),output);
    }

    @Test
    public void getArticleFromUserInputCorrectDOIPrefixSuffixFormatTest(){
        //GIVEN: A correct doi (Prefix/Suffix Format)
        String correctDOI = "10.1126/science.370.6523.1384";

        //WHEN: The getArticleFromUserInputDOI is called with the correct doi
        Article output = articleService.getArticleFromUserInputDOI(correctDOI);

        //THEN: The output must be distinct from null
        assertNotNull(output);
        //AND: The output must be the article with the correct doi stored in the database
        assertEquals(articleRepository.findFirstByDOI("https://doi.org/"+correctDOI),output);
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

    @Test
    public void getArticleFromNullDOIListTest(){
        //GIVEN: A null list
        List<String> nullDOIList = null;

        //WHEN: The getArticlesFromDOIList is called with the null doi list
        List<Article> output = articleService.getArticlesFromDOIList(nullDOIList);

        //THEN: The output must be an empty list
        assertTrue(output.isEmpty());
    }

    @Test
    public void getArticleFromListWithNullDOITest(){
        //GIVEN: A list with a null object
        List<String> nullDOIList = Arrays.asList(new String[]{null});

        //WHEN: The getArticlesFromDOIList is called with the doi list with a null object
        List<Article> output = articleService.getArticlesFromDOIList(nullDOIList);

        //THEN: The output must be an empty list
        assertTrue(output.isEmpty());
    }

    @Test
    public void getArticleFromListWithMalformedDOITest(){
        //GIVEN: A list with a malformed doi
        List<String> malformedDOIList = Arrays.asList(new String[]{"malformed"});

        //WHEN: The getArticlesFromDOIList is called with the doi list with a malformed doi
        List<Article> output = articleService.getArticlesFromDOIList(malformedDOIList);

        //THEN: The output must be an empty list
        assertTrue(output.isEmpty());
    }

    @Test
    public void getArticleFromListWithErroneousDOITest(){
        //GIVEN: A list with an erroneous doi
        List<String> erroneousDOIList = Arrays.asList(new String[]{"http://www.erroneousDOI.com"});

        //WHEN: The getArticlesFromDOIList is called with the doi list with an erroneous doi
        List<Article> output = articleService.getArticlesFromDOIList(erroneousDOIList);

        //THEN: The output must be an empty list
        assertTrue(output.isEmpty());
    }

    @Test
    public void getArticleFromListWithDOIWithStatusCodeDistinctTo200Test(){
        //GIVEN: A list with a doi that returns 404 Not Found error
        List<String> DOIList404 = Arrays.asList(new String[]{"http://www.google.com/erroneousDOI"});

        //WHEN: The getArticlesFromDOIList is called with the doi list with a doi that returns 404
        List<Article> output = articleService.getArticlesFromDOIList(DOIList404);

        //THEN: The output must be an empty list
        assertTrue(output.isEmpty());
    }

    @Test
    public void getArticleFromListWIthCorrectDOITest(){
        //GIVEN: A correct doi
        String correctDOI = "https://doi.org/10.1126/science.370.6523.1384";

        //AND: A list with a correct doi
        List<String> doiList = Arrays.asList(new String[]{correctDOI});

        //WHEN: The getArticlesFromDOIList is called with the doi list
        List<Article> output = articleService.getArticlesFromDOIList(doiList);

        //THEN: The output must be not empty
        assertFalse(output.isEmpty());
        //AND: The output must be a list that contains the article with the correct doi stored in the database
        assertTrue(output.contains(articleRepository.findFirstByDOI(correctDOI)));
    }

    @Test
    public void getArticleFromListWIthCorrectDOIAndIncorrectDOITest(){
        //GIVEN: A correct doi
        String correctDOI = "https://doi.org/10.1126/science.370.6523.1384";

        //AND: A list with a correct doi and an incorrect doi
        List<String> doiList = Arrays.asList(new String[]{correctDOI,"https://dsvfbnhsgfds.com"});

        //WHEN: The getArticlesFromDOIList is called with the doi list
        List<Article> output = articleService.getArticlesFromDOIList(doiList);

        //THEN: The output must be not empty
        assertFalse(output.isEmpty());
        //AND: The output must be a list that contains the article with the correct doi stored in the database
        assertTrue(output.contains(articleRepository.findFirstByDOI(correctDOI)));
    }

}
