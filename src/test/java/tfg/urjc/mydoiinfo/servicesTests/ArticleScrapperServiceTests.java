package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.*;
import tfg.urjc.mydoiinfo.services.ArticleScrapperService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArticleScrapperServiceTests {
    @Autowired
    ArticleScrapperService articleScrapperService;

    @Test
    public void getArticleFromNullDOITest(){
        //GIVEN: A null doi
        String nullDOI = null;

        //WHEN: The getArticleInfoFromDOI is called with the null doi
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(nullDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromMalformedDOITest(){
        //GIVEN: A fake doi
        String malformedDOI = "malformed";

        //WHEN: The getArticleInfoFromDOI is called with the malformed doi
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(malformedDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromErroneousDOITest(){
        //GIVEN: A erroneous doi
        String erroneousDOI = "http://www.erroneousDOI.com";

        //WHEN: The getArticleInfoFromDOI is called with the erroneous doi
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(erroneousDOI);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromDOIWithStatusCodeDistinctTo200Test(){
        //GIVEN: A doi that returns 404 Not Found error
        String doi404 = "http://www.google.com/erroneousDOI";

        //WHEN: The getArticleInfoFromDOI is called with the 404 error doi
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(doi404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromACMDOITest(){
        //GIVEN: An ACMArticleScrapper
        ACMArticleScrapper acmArticleScrapper = new ACMArticleScrapper(new String[]{"10.1145"});
        //AND: A correct doi from ACM
        String correctACMDOI = "https://doi.org/10.1145/3488554";
        //WHEN: The getArticleInfoFromDOI method of the ArticleScrapperService is called with the correct doi from ACM
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(correctACMDOI);
        //THEN: The output must be the same as the return value of the getArticleInfoFromDOI method of the
        //ACMArticleScrapper when it is called with the correct doi from ACM
        assertEquals(acmArticleScrapper.getArticleInfoFromDOI(correctACMDOI),output);
    }

    @Test
    public void getArticleInfoFromElsevierDOITest(){
        //GIVEN: An ElsevierArticleScrapper
        ElsevierArticleScrapper elsevierArticleScrapper = new ElsevierArticleScrapper(new String[]{"10.1016"});
        //AND: A correct doi from Elsevier
        String correctElsevierDOI = "https://doi.org/10.1016/j.ecoleng.2014.09.079";
        //WHEN: The getArticleInfoFromDOI method of the ArticleScrapperService is called with the correct doi from Elsevier
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(correctElsevierDOI);
        //THEN: The output must be the same as the return value of the getArticleInfoFromDOI method of the
        //ElsevierArticleScrapper when it is called with the correct doi from Elsevier
        assertEquals(elsevierArticleScrapper.getArticleInfoFromDOI(correctElsevierDOI),output);
    }

    @Test
    public void getArticleInfoFromIEEEDOITest(){
        //GIVEN: An IEEEArticleScrapper
        IEEEArticleScrapper ieeeArticleScrapper = new IEEEArticleScrapper(new String[]{"10.1109"});
        //AND: A correct doi from IEEE
        String correctIEEEDOI = "https://doi.org/10.1109/101.8118";
        //WHEN: The getArticleInfoFromDOI method of the ArticleScrapperService is called with the correct doi from IEEE
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(correctIEEEDOI);
        //THEN: The output must be the same as the return value of the getArticleInfoFromDOI method of the
        //IEEEArticleScrapper when it is called with the correct doi from IEEE
        assertEquals(ieeeArticleScrapper.getArticleInfoFromDOI(correctIEEEDOI),output);
    }

    @Test
    public void getArticleInfoFromScienceDOITest(){
        //GIVEN: An ScienceArticleScrapper
        ScienceArticleScrapper scienceArticleScrapper = new ScienceArticleScrapper(new String[]{"10.1126"});
        //AND: A correct doi from Science
        String correctScienceDOI = "https://doi.org/10.1126/science.abf1015";
        //WHEN: The getArticleInfoFromDOI method of the ArticleScrapperService is called with the correct doi from Science
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(correctScienceDOI);
        //THEN: The output must be the same as the return value of the getArticleInfoFromDOI method of the
        //ScienceArticleScrapper when it is called with the correct doi from Science
        assertEquals(scienceArticleScrapper.getArticleInfoFromDOI(correctScienceDOI),output);
    }

    @Test
    public void getArticleInfoFromSpringerDOITest(){
        //GIVEN: An SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(new String[]{"10.1134"});
        //AND: A correct doi from Springer
        String correctSpringerDOI = "https://doi.org/10.1134/S0965542519090069";
        //WHEN: The getArticleInfoFromDOI method of the ArticleScrapperService is called with the correct doi from Springer
        ArticleInfo output = articleScrapperService.getArticleInfoFromDOI(correctSpringerDOI);
        //THEN: The output must be the same as the return value of the getArticleInfoFromDOI method of the
        //SpringerArticleScrapper when it is called with the correct doi from Springer
        assertEquals(springerArticleScrapper.getArticleInfoFromDOI(correctSpringerDOI),output);
    }

    @Test
    public void existsArticleScrapperForDOITestNullDoi(){
        //GIVEN: A null DOI
        String doi = null;

        //WHEN: The existsArticleScrapperForDOI is called with the DOI
        boolean output = articleScrapperService.existsArticleScrapperForDOI(doi);

        //THEN: The output must be false
        assertFalse(output);
    }

    @Test
    public void existsArticleScrapperForDOITestMalformedDoi(){
        //GIVEN: A malformed DOI
        String doi = "ukvbiliibukvb10.1145uykgkiwldba997876";

        //WHEN: The existsArticleScrapperForDOI is called with the DOI
        boolean output = articleScrapperService.existsArticleScrapperForDOI(doi);

        //THEN: The output must be false
        assertFalse(output);
    }

    @Test
    public void existsArticleScrapperForDOITestNotScrapperForDoi(){
        //GIVEN: A DOI of a publisher its scrapper it's not created
        String doi = "https://doi.org/8.234521423542546/10.1126.10.1145/";

        //WHEN: The existsArticleScrapperForDOI is called with the DOI
        boolean output = articleScrapperService.existsArticleScrapperForDOI(doi);

        //THEN: The output must be false
        assertFalse(output);
    }

    @Test
    public void existsArticleScrapperForDOITestExistsScrapperForDoi(){
        //GIVEN: A DOI of a publisher its scrapper it's created
        String doi = "https://doi.org/10.1126/science.abb3420";

        //WHEN: The existsArticleScrapperForDOI is called with the DOI
        boolean output = articleScrapperService.existsArticleScrapperForDOI(doi);

        //THEN: The output must be true
        assertTrue(output);
    }

    @Test
    public void existsArticleScrapperForDOITestExistsScrapperForDoiNotURLFormat(){
        //GIVEN: A DOI of a publisher its scrapper it's created (Not url format)
        String doi = "10.1126/science.abb3420";

        //WHEN: The existsArticleScrapperForDOI is called with the DOI
        boolean output = articleScrapperService.existsArticleScrapperForDOI(doi);

        //THEN: The output must be true
        assertTrue(output);
    }
}
