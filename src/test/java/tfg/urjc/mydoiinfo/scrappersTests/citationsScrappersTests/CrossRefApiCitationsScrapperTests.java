package tfg.urjc.mydoiinfo.scrappersTests.citationsScrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.scrappers.citationsScrappers.CrossRefApiCitationsScrapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CrossRefApiCitationsScrapperTests {

    @Test
    public void getCitationsFromNullArticleTest(){
        //GIVEN: The CrossRefApiCitationsScrapper
        CrossRefApiCitationsScrapper crossRefApiCitationsScrapper = new CrossRefApiCitationsScrapper();
        //AND: A null article
        Article nullArticle = null;

        //WHEN: The getCitationsFromArticle is called with the null Article
        Long output = crossRefApiCitationsScrapper.getCitationsFromArticle(nullArticle);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleWithMalformedDoiTest(){
        //GIVEN: The CrossRefApiCitationsScrapper
        CrossRefApiCitationsScrapper crossRefApiCitationsScrapper = new CrossRefApiCitationsScrapper();
        //AND: An article with a malformed DOI
        Article malformedDoiArticle = new Article();
        malformedDoiArticle.setDOI("testmalformeddoi");

        //WHEN: The getCitationsFromArticle is called with the Article
        Long output = crossRefApiCitationsScrapper.getCitationsFromArticle(malformedDoiArticle);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleWithErroneousDoiTest(){
        //GIVEN: The CrossRefApiCitationsScrapper
        CrossRefApiCitationsScrapper crossRefApiCitationsScrapper = new CrossRefApiCitationsScrapper();
        //AND: An article with an erroneous DOI
        Article erroneousDoiArticle = new Article();
        erroneousDoiArticle.setDOI("http://www.erroneousDOI.com");

        //WHEN: The getCitationsFromArticle is called with the Article
        Long output = crossRefApiCitationsScrapper.getCitationsFromArticle(erroneousDoiArticle);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleWithCorrectDoiTest(){
        //GIVEN: The CrossRefApiCitationsScrapper
        CrossRefApiCitationsScrapper crossRefApiCitationsScrapper = new CrossRefApiCitationsScrapper();
        //AND: An article with a correct DOI
        Article correctDoiArticle = new Article();
        correctDoiArticle.setDOI("https://doi.org/10.1016/j.arabjc.2017.05.011");

        //WHEN: The getCitationsFromArticle is called with the Article
        Long output = crossRefApiCitationsScrapper.getCitationsFromArticle(correctDoiArticle);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output must be greater or equals than 1225 (citations number grows steadily)
        assertTrue(output>=1225);
    }

}
