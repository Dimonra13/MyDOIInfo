package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.services.CitationsScrapperService;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class CitationsScrapperServiceTests {

    @Autowired
    CitationsScrapperService citationsScrapperService;

    @Test
    public void getCitationsFromNullArticleTest(){
        //GIVEN: A null article
        Article nullArticle = null;

        //WHEN: The getCitationsFromArticle is called with the null Article
        Long output = citationsScrapperService.getCitationsFromArticle(nullArticle);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleWithMalformedDoiTest(){
        //GIVEN:  An article with a malformed DOI
        Article malformedDoiArticle = new Article();
        malformedDoiArticle.setDOI("testmalformeddoi");

        //WHEN: The getCitationsFromArticle is called with the Article
        Long output = citationsScrapperService.getCitationsFromArticle(malformedDoiArticle);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleWithErroneousDoiTest(){
        //GIVEN: An article with an erroneous DOI
        Article erroneousDoiArticle = new Article();
        erroneousDoiArticle.setDOI("http://www.erroneousDOI.com");

        //WHEN: The getCitationsFromArticle is called with the Article
        Long output = citationsScrapperService.getCitationsFromArticle(erroneousDoiArticle);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleWithCorrectDoiTest(){
        //GIVEN: An article with a correct DOI
        Article correctDoiArticle = new Article();
        correctDoiArticle.setDOI("https://doi.org/10.1145/988772.988777");

        //WHEN: The getCitationsFromArticle is called with the Article
        Long output = citationsScrapperService.getCitationsFromArticle(correctDoiArticle);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output must be greater or equals than 183 (citations number grows steadily)
        assertTrue(output>=183);
    }

    @Test
    public void getCitationsFromNullArticleInfoTest(){
        //GIVEN: A null articleInfo
        ArticleInfo nullArticleInfo = null;

        //WHEN: The getCitationsFromArticleInfo is called with the null ArticleInfo
        Long output = citationsScrapperService.getCitationsFromArticleInfo(nullArticleInfo);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleInfoWithMalformedDoiTest(){
        //GIVEN:  An articleInfo with a malformed DOI
        ArticleInfo malformedDoiArticleInfo = new ArticleInfo();
        malformedDoiArticleInfo.setDOI("testmalformeddoi");

        //WHEN: The getCitationsFromArticleInfo is called with the ArticleInfo
        Long output = citationsScrapperService.getCitationsFromArticleInfo(malformedDoiArticleInfo);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleInfoWithErroneousDoiTest(){
        //GIVEN: An articleInfo with an erroneous DOI
        ArticleInfo erroneousDoiArticleInfo = new ArticleInfo();
        erroneousDoiArticleInfo.setDOI("http://www.erroneousDOI.com");

        //WHEN: The getCitationsFromArticleInfo is called with the ArticleInfo
        Long output = citationsScrapperService.getCitationsFromArticleInfo(erroneousDoiArticleInfo);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getCitationsFromArticleInfoWithCorrectDoiTest(){
        //GIVEN: An articleInfo with a correct DOI
        ArticleInfo correctDoiArticleInfo = new ArticleInfo();
        correctDoiArticleInfo.setDOI("https://doi.org/10.1145/988772.988777");

        //WHEN: The getCitationsFromArticleInfo is called with the ArticleInfo
        Long output = citationsScrapperService.getCitationsFromArticleInfo(correctDoiArticleInfo);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output must be greater or equals than 183 (citations number grows steadily)
        assertTrue(output>=183);
    }
}
