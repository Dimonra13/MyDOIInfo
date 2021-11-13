package tfg.urjc.mydoiinfo.scrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.ACMArticleScrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ACMArticleScrapperTests {

    private String[] journalPrefixList = new String[]{"10.1145"};

    @Test
    public void getArticleInfoFromNullURLTest(){
        //GIVEN: The ACMArticleScrapper
        ACMArticleScrapper acmArticleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A null url
        String nullURL = null;

        //WHEN: The getArticleInfoFromDOI is called with the null URL
        ArticleInfo output = acmArticleScrapper.getArticleInfoFromDOI(nullURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromMalformedURLTest(){
        //GIVEN: The ACMArticleScrapper
        ACMArticleScrapper acmArticleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A fake url
        String malformedURL = "malformed";

        //WHEN: The getArticleInfoFromDOI is called with the malformed URL
        ArticleInfo output = acmArticleScrapper.getArticleInfoFromDOI(malformedURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromErroneousURLTest(){
        //GIVEN: The ACMArticleScrapper
        ACMArticleScrapper acmArticleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A erroneous url
        String erroneousURL = "http://www.erroneousURL.com";

        //WHEN: The getArticleInfoFromDOI is called with the erroneous URL
        ArticleInfo output = acmArticleScrapper.getArticleInfoFromDOI(erroneousURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromURLWithStatusCodeDistinctTo200Test(){
        //GIVEN: The ACMArticleScrapper
        ACMArticleScrapper acmArticleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A url that returns 404 Not Found error
        String url404 = "http://www.google.com/erroneousURL";

        //WHEN: The getArticleInfoFromDOI is called with the 404 error url
        ArticleInfo output = acmArticleScrapper.getArticleInfoFromDOI(url404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromCorrectURLTest(){
        //GIVEN: The ACMArticleScrapper
        ACMArticleScrapper acmArticleScrapper = new ACMArticleScrapper(journalPrefixList);
        //AND: A correct url from ACM
        String correctACM = "https://doi.org/10.1145/3488554";
        //AND: The expected output (an ArticleInfo object with the correct information)
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        try {
            date = format.parse("25 October 2021");
        } catch (ParseException exception) {
            System.err.println(exception);
        }
        List<String> authors = new LinkedList<>();
        authors.add("Moshe Y. Vardi");
        ArticleInfo expectedOutput = new ArticleInfo("The paradox of choice in computing-research conferences","https://doi.org/10.1145/3488554",
                authors,"Communications of the ACM","Volume 64, pp 5",date,"25 October 2021");

        //WHEN: The getArticleInfoFromDOI is called with the correct ACM url
        ArticleInfo output = acmArticleScrapper.getArticleInfoFromDOI(correctACM);

        //THEN: The output must the same as the expectedOutput
        assertEquals(expectedOutput,output);
    }

}
