package tfg.urjc.mydoiinfo.scrappersTests.articleScrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.SpringerArticleScrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class SpringerArticleScrapperTests {

    private String[] journalPrefixList = new String[]{"10.1134","10.0007"};

    @Test
    public void getArticleInfoFromNullURLTest(){
        //GIVEN: The SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(journalPrefixList);
        //AND: A null url
        String nullURL = null;

        //WHEN: The getArticleInfoFromDOI is called with the null URL
        ArticleInfo output = springerArticleScrapper.getArticleInfoFromDOI(nullURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromMalformedURLTest(){
        //GIVEN: The SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(journalPrefixList);
        //AND: A fake url
        String malformedURL = "malformed";

        //WHEN: The getArticleInfoFromDOI is called with the malformed URL
        ArticleInfo output = springerArticleScrapper.getArticleInfoFromDOI(malformedURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromErroneousURLTest(){
        //GIVEN: The SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(journalPrefixList);
        //AND: A erroneous url
        String erroneousURL = "http://www.erroneousURL.com";

        //WHEN: The getArticleInfoFromDOI is called with the erroneous URL
        ArticleInfo output = springerArticleScrapper.getArticleInfoFromDOI(erroneousURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromURLWithStatusCodeDistinctTo200Test(){
        //GIVEN: The SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(journalPrefixList);
        //AND: A url that returns 404 Not Found error
        String url404 = "http://www.google.com/erroneousURL";

        //WHEN: The getArticleInfoFromDOI is called with the 404 error url
        ArticleInfo output = springerArticleScrapper.getArticleInfoFromDOI(url404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromCorrectURLTest(){
        //GIVEN: The SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(journalPrefixList);
        //AND: A correct url from Springer
        String correctSpringer = "https://doi.org/10.1134/S0965542519090069";
        //AND: The expected output (an ArticleInfo object with the correct information)
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        try {
            date = format.parse("15 October 2019");
        } catch (ParseException exception) {
            System.err.println(exception);
        }
        List<String> authors = new LinkedList<>();
        authors.add("V. N. Babenko");
        ArticleInfo expectedOutput = new ArticleInfo("On the Structure of Closeness Estimates for Pseudosolutions of Initial and Perturbed Systems of Linear Algebraic Equations",
                "https://doi.org/10.1134/S0965542519090069",authors,"Computational Mathematics and Mathematical Physics","volume 59, pp 1399–1421",date,"15 October 2019");

        //WHEN: The getArticleInfoFromDOI is called with the correct Springer url
        ArticleInfo output = springerArticleScrapper.getArticleInfoFromDOI(correctSpringer);

        //THEN: The output must the same as the expectedOutput
        assertEquals(expectedOutput,output);
    }

    @Test
    public void getArticleInfoFromCorrectURLWithSecondaryPrefixTest(){
        //GIVEN: The SpringerArticleScrapper
        SpringerArticleScrapper springerArticleScrapper = new SpringerArticleScrapper(journalPrefixList);
        //AND: A correct url from Springer
        String correctSpringer = "https://doi.org/10.1007/s12310-021-09471-5";
        //AND: The expected output (an ArticleInfo object with the correct information)
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        try {
            date = format.parse("18 August 2021");
        } catch (ParseException exception) {
            System.err.println(exception);
        }
        List<String> authors = new LinkedList<>();
        authors.add("Amanda B. Nickerson");
        authors.add("Jamie M. Ostrov");
        ArticleInfo expectedOutput = new ArticleInfo("Protective Factors and Working with Students Involved in Bullying: Commentary on the Special Issue and Dedication to Dan Olweus",
                "https://doi.org/10.1007/s12310-021-09471-5",authors,"School Mental Health","volume 13, pp 443–451",date,"18 August 2021");

        //WHEN: The getArticleInfoFromDOI is called with the correct Springer url
        ArticleInfo output = springerArticleScrapper.getArticleInfoFromDOI(correctSpringer);

        //THEN: The output must the same as the expectedOutput
        assertEquals(expectedOutput,output);
    }

}
