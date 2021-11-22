package tfg.urjc.mydoiinfo.scrappersTests.citationsScrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.citationsScrappers.CrossRefApiCitationsScrapper;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ApiCitationsScrapperTests {
    @Test
    public void getResponseFromURLTestStatusCode200OK(){
        //GIVEN: A correct url
        String correctUrl = "https://www.google.com/";

        //WHEN: The getResponseFromURL is called (using a subclass to access the protected method)
        Response response = new CrossRefApiCitationsScrapper(){
            public Response getResponseFromURL(String url){
                return super.getResponseFromURL(url);
            }
        }.getResponseFromURL(correctUrl);

        //THEN: The statusCode must be 200
        assertEquals(200,response.getStatus());
    }

    @Test
    public void getResponseFromURLTestStatusCode404NotFound(){
        //GIVEN: A non-existing page url
        String notFoundUrl = "https://www.google.com/not_found";

        //WHEN: The getResponseFromURL is called (using a subclass to access the protected method)
        Response response = new CrossRefApiCitationsScrapper(){
            public Response getResponseFromURL(String url){
                return super.getResponseFromURL(url);
            }
        }.getResponseFromURL(notFoundUrl);

        //THEN: The statusCode must be 404
        assertEquals(404,response.getStatus());
    }

    @Test
    public void getResponseFromURLTestWrongURL(){
        //GIVEN: A wrong url
        String wrongUrl = "wrongurl";

        //WHEN: The getResponseFromURL is called (using a subclass to access the protected method)
        Response response = new CrossRefApiCitationsScrapper(){
            public Response getResponseFromURL(String url){
                return super.getResponseFromURL(url);
            }
        }.getResponseFromURL(wrongUrl);

        //THEN: The response must be null
        assertNull(response);
    }

    @Test
    public void getResponseFromNullURLTest(){
        //GIVEN: A null url
        String nullUrl = null;

        //WHEN: The getResponseFromURL is called (using a subclass to access the protected method)
        Response response = new CrossRefApiCitationsScrapper(){
            public Response getResponseFromURL(String url){
                return super.getResponseFromURL(url);
            }
        }.getResponseFromURL(nullUrl);

        //THEN: The response must be null
        assertNull(response);
    }

}
