package tfg.urjc.mydoiinfo.scrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.ACMArticleScrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScrapperTests {
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

    @Test
    public void getHTTPStatusCodeTestNullUrl(){
        //GIVEN: A null url
        String nullUrl = null;

        //WHEN: The getHTTPStatusCode is called (using a subclass to access the protected method)
        int statusCode = new ACMArticleScrapper(new String[]{"10.1145"}){
            public int getHTTPStatusCode(String url){
                return super.getHTTPStatusCode(url);
            }
        }.getHTTPStatusCode(nullUrl);

        //THEN: The statusCode must be 400
        assertEquals(400,statusCode);
    }
}
