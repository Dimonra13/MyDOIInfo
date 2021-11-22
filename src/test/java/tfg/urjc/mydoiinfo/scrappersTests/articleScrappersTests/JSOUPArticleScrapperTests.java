package tfg.urjc.mydoiinfo.scrappersTests.articleScrappersTests;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.ACMArticleScrapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JSOUPArticleScrapperTests {

    private String[] journalPrefixList = new String[]{"10.1145"};

    @Test
    public void getHtmlDocumentTest200OK(){
        //GIVEN: A correct url
        String correctUrl = "https://www.google.com/";

        //WHEN: The getHtmlDocument is called (using a subclass to access the protected method)
        Document document = new ACMArticleScrapper(journalPrefixList){
            public Document getHtmlDocument(String url){
                return super.getHtmlDocument(url);
            }
        }.getHtmlDocument(correctUrl);

        //THEN: The html must be distinct from null
        assertNotNull(document);
    }

    @Test
    public void getHtmlDocumentTest404NotFound(){
        //GIVEN: A non-existing page url
        String notFoundUrl = "https://www.google.com/not_found";

        //WHEN: The getHtmlDocument is called (using a subclass to access the protected method)
        Document document = new ACMArticleScrapper(journalPrefixList){
            public Document getHtmlDocument(String url){
                return super.getHtmlDocument(url);
            }
        }.getHtmlDocument(notFoundUrl);

        //THEN: The html must be null
        assertNull(document);
    }

    @Test
    public void getHtmlDocumentTest400BadRequest(){
        //GIVEN: A wrong url
        String wrongUrl = "wrongurl";

        //WHEN: The getHtmlDocument is called (using a subclass to access the protected method)
        Document document = new ACMArticleScrapper(journalPrefixList){
            public Document getHtmlDocument(String url){
                return super.getHtmlDocument(url);
            }
        }.getHtmlDocument(wrongUrl);

        //THEN: The html must be null
        assertNull(document);
    }

    @Test
    public void getHtmlDocumentTestNullUrl(){
        //GIVEN: A null url
        String nullUrl = null;

        //WHEN: The getHtmlDocument is called (using a subclass to access the protected method)
        Document document = new ACMArticleScrapper(journalPrefixList){
            public Document getHtmlDocument(String url){
                return super.getHtmlDocument(url);
            }
        }.getHtmlDocument(nullUrl);

        //THEN: The html must be null
        assertNull(document);
    }
}
