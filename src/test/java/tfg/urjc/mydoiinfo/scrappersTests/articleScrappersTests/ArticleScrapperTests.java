package tfg.urjc.mydoiinfo.scrappersTests.articleScrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.ACMArticleScrapper;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.ArticleScrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ArticleScrapperTests {

    @Test
    public void isCorrectJournalTest(){
        //GIVEN: A specific journalPrefix (ACM)
        String[] journalPrefixList = new String[]{"10.1145"};
        //AND: An article Scrapper with that prefix
        ArticleScrapper articleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A url with the correct prefix
        String url = "https://doi.org/10.1145/3488554";

        //WHEN: The isCorrectJournalMethod is called
        boolean output = articleScrapper.isCorrectJournalScrapper(url);

        //THEN: The output must be true
        assertEquals(true,output);
    }

    @Test
    public void isWrongJournalPrefixTest(){
        //GIVEN: A specific journalPrefix (ACM)
        String[] journalPrefixList = new String[]{"10.1145"};
        //AND: An article Scrapper with that prefix
        ArticleScrapper articleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A url with the incorrect prefix
        String url = "https://doi.org/10.1109/JIOT.2020.2983228";

        //WHEN: The isCorrectJournalMethod is called
        boolean output = articleScrapper.isCorrectJournalScrapper(url);

        //THEN: The output must be false
        assertEquals(false,output);
    }

    @Test
    public void isIncorrectJournalURLTest(){
        //GIVEN: A specific journalPrefix (ACM)
        String[] journalPrefixList = new String[]{"10.1145"};
        //AND: An article Scrapper with that prefix
        ArticleScrapper articleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A wrong url
        String url = "https://www.google.com/";

        //WHEN: The isCorrectJournalMethod is called
        boolean output = articleScrapper.isCorrectJournalScrapper(url);

        //THEN: The output must be false
        assertEquals(false,output);
    }
}
