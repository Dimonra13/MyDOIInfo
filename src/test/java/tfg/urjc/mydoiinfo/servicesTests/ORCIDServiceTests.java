package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.services.ORCIDService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ORCIDServiceTests {

    @Autowired
    ORCIDService orcidService;

    @Test
    public void getAuthorNameFromNullORCIDidTest(){
        //GIVEN: A null ORCID id
        String id = null;

        //WHEN: The getAuthorNameFromORCIDid method is called with the specified id
        String authorName = orcidService.getAuthorNameFromORCIDId(id);

        //THEN: The authorName must be null
        assertNull(authorName);
    }

    @Test
    public void getAuthorNameFromMalformedORCIDidTest(){
        //GIVEN: A malformed ORCID id
        String id = "786758786568657";

        //WHEN: The getAuthorNameFromORCIDid method is called with the specified id
        String authorName = orcidService.getAuthorNameFromORCIDId(id);

        //THEN: The authorName must be null
        assertNull(authorName);
    }

    @Test
    public void getAuthorNameFromCorrectORCIDidWithCreditNameTest(){
        //GIVEN: A correct ORCID id (with a credit name set)
        String id = "0000-0001-5727-2427";
        //AND: The expected name
        String expectedName = "Sofia Maria Hernandez Garcia";

        //WHEN: The getAuthorNameFromORCIDid method is called with the specified id
        String authorName = orcidService.getAuthorNameFromORCIDId(id);

        //THEN: The authorName must not be null
        assertNotNull(authorName);
        //AND: The authorName must be equals to the expectedName
        assertEquals(expectedName,authorName);
    }

    @Test
    public void getAuthorNameFromCorrectORCIDidWithoutCreditNameTest(){
        //GIVEN: A correct ORCID id (without a credit name set)
        String id = "0000-0002-5382-6805";
        //AND: The expected name
        String expectedName = "Alberto Sanchez";

        //WHEN: The getAuthorNameFromORCIDid method is called with the specified id
        String authorName = orcidService.getAuthorNameFromORCIDId(id);

        //THEN: The authorName must not be null
        assertNotNull(authorName);
        //AND: The authorName must be equals to the expectedName
        assertEquals(expectedName,authorName);
    }

    @Test
    public void getArticlesFromNullORCIDidTest(){
        //GIVEN: A null ORCID id
        String id = null;

        //WHEN: The getArticlesFromORCIDid method is called with the specified id
        List<Article> articleList = orcidService.getArticlesFromORCIDid(id);

        //THEN: The articleList must be null
        assertNull(articleList);
    }

    @Test
    public void getArticlesFromMalformedORCIDidTest(){
        //GIVEN: A malformed ORCID id
        String id = "786758786568657";

        //WHEN: The getArticlesFromORCIDid method is called with the specified id
        List<Article> articleList = orcidService.getArticlesFromORCIDid(id);

        //THEN: The articleList must be null
        assertNull(articleList);
    }

    @Test
    public void getArticlesFromCorrectORCIDidTest(){
        //GIVEN: A correct ORCID id
        String id = "0000-0001-5727-2427";

        //WHEN: The getArticlesFromORCIDid method is called with the specified id
        List<Article> articleList = orcidService.getArticlesFromORCIDid(id);

        //THEN: The articleList must not be null
        assertNotNull(articleList);
        //AND: The articleList size must be greater than or equal to five
        assertTrue(articleList.size()>=5);
    }
}
