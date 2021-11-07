package tfg.urjc.mydoiinfo.scrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.ACMArticleScrapper;
import tfg.urjc.mydoiinfo.scrappers.ArticleScrapper;

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

    @Test
    public void getHTTPStatusCodeTest200OK(){
        //GIVEN: A correct url
        String correctUrl = "https://www.google.com/";

        //WHEN: The getHTTPStatusCode is called (using a subclass to access the protected method)
        int statusCode = new ACMArticleScrapper(new String[]{"10.1145"}){
            public int getHTTPStatusCode(String url){
                return super.getHTTPStatusCode(url);
            }
        }.getHTTPStatusCode(correctUrl);

        //THEN: The statusCode must be 200
        assertEquals(200,statusCode);
    }

    @Test
    public void getHTTPStatusCodeTest404NotFound(){
        //GIVEN: A non-existing page url
        String notFoundUrl = "https://www.google.com/not_found";

        //WHEN: The getHTTPStatusCode is called (using a subclass to access the protected method)
        int statusCode = new ACMArticleScrapper(new String[]{"10.1145"}){
            public int getHTTPStatusCode(String url){
                return super.getHTTPStatusCode(url);
            }
        }.getHTTPStatusCode(notFoundUrl);

        //THEN: The statusCode must be 404
        assertEquals(404,statusCode);
    }

    @Test
    public void getHTTPStatusCodeTest400BadRequest(){
        //GIVEN: A wrong url
        String wrongUrl = "wrongurl";

        //WHEN: The getHTTPStatusCode is called (using a subclass to access the protected method)
        int statusCode = new ACMArticleScrapper(new String[]{"10.1145"}){
            public int getHTTPStatusCode(String url){
                return super.getHTTPStatusCode(url);
            }
        }.getHTTPStatusCode(wrongUrl);

        //THEN: The statusCode must be 400
        assertEquals(400,statusCode);
    }
}
