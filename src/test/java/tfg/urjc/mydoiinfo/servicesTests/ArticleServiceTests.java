package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.services.ArticleService;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ArticleServiceTests {

    @Autowired
    ArticleService articleService;

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
}
