package tfg.urjc.mydoiinfo.scrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.ScienceArticleScrapper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ScienceArticleScrapperTests {

    private String[] journalPrefixList = new String[]{"10.1126"};

    @Test
    public void getArticleInfoFromNullURLTest(){
        //GIVEN: The ScienceArticleScrapper
        ScienceArticleScrapper scienceArticleScrapper = new ScienceArticleScrapper(journalPrefixList);
        //AND: A null url
        String nullURL = null;

        //WHEN: The getArticleInfoFromDOI is called with the null URL
        ArticleInfo output = scienceArticleScrapper.getArticleInfoFromDOI(nullURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromMalformedURLTest(){
        //GIVEN: The ScienceArticleScrapper
        ScienceArticleScrapper scienceArticleScrapper = new ScienceArticleScrapper(journalPrefixList);
        //AND: A fake url
        String malformedURL = "malformed";

        //WHEN: The getArticleInfoFromDOI is called with the malformed URL
        ArticleInfo output = scienceArticleScrapper.getArticleInfoFromDOI(malformedURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromErroneousURLTest(){
        //GIVEN: The ScienceArticleScrapper
        ScienceArticleScrapper scienceArticleScrapper = new ScienceArticleScrapper(journalPrefixList);
        //AND: A erroneous url
        String erroneousURL = "http://www.erroneousURL.com";

        //WHEN: The getArticleInfoFromDOI is called with the erroneous URL
        ArticleInfo output = scienceArticleScrapper.getArticleInfoFromDOI(erroneousURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromURLWithStatusCodeDistinctTo200Test(){
        //GIVEN: The ScienceArticleScrapper
        ScienceArticleScrapper scienceArticleScrapper = new ScienceArticleScrapper(journalPrefixList);
        //AND: A url that returns 404 Not Found error
        String url404 = "http://www.google.com/erroneousURL";

        //WHEN: The getArticleInfoFromDOI is called with the 404 error url
        ArticleInfo output = scienceArticleScrapper.getArticleInfoFromDOI(url404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromCorrectURLTest(){
        //GIVEN: The ScienceArticleScrapper
        ScienceArticleScrapper scienceArticleScrapper = new ScienceArticleScrapper(journalPrefixList);
        //AND: A correct url from Science
        String correctScience = "https://doi.org/10.1126/science.abf1015";
        //AND: The expected output (an ArticleInfo object with the correct information)
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        try {
            date = format.parse("28 Oct 2021");
        } catch (ParseException exception) {
            System.err.println(exception);
        }
        List<String> authors = Arrays.asList(new String[]{"S. J. Bolton", "S. Levin", "T. Guillot", "C. Li", "Y. Kaspi", "G. Orton", "M. H. Wong", "F. Oyafuso", "M. Allison",
                "J. Arballo", "S. Atreya", "H. N. Becker", "J. Bloxham", "S. Brown", "L. N. Fletcher", "E. Galanti", "S. Gulkis", "M. Janssen", "A. Ingersoll",
                "J. L. Lunine", "S. Misra", "P. Steffes", "D. Stevenson", "J. H. Waite", "R. K. Yadav", "Z. Zhang"});
        ArticleInfo expectedOutput = new ArticleInfo("Microwave observations reveal the deep extent and structure of Jupiter’s atmospheric vortices","https://doi.org/10.1126/science.abf1015",
                authors,"Science","First Release",date,"28 Oct 2021");

        //WHEN: The getArticleInfoFromDOI is called with the correct Science url
        ArticleInfo output = scienceArticleScrapper.getArticleInfoFromDOI(correctScience);

        //THEN: The output must the same as the expectedOutput
        assertEquals(expectedOutput,output);
    }

}
