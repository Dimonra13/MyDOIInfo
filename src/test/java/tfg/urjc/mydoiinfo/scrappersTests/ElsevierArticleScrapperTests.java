package tfg.urjc.mydoiinfo.scrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.domain.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.ElsevierArticleScrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ElsevierArticleScrapperTests {

    private String journalPrefix = "10.1016";

    @Test
    public void getArticleInfoFromMalformedURLTest(){
        //GIVEN: The ElsevierArticleScrapper
        ElsevierArticleScrapper elsevierArticleScrapper = new ElsevierArticleScrapper(journalPrefix);
        //AND: A fake url
        String malformedURL = "malformed";

        //WHEN: The getArticleInfoFromDOI is called with the malformed URL
        ArticleInfo output = elsevierArticleScrapper.getArticleInfoFromDOI(malformedURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromErroneousURLTest(){
        //GIVEN: The ElsevierArticleScrapper
        ElsevierArticleScrapper elsevierArticleScrapper = new ElsevierArticleScrapper(journalPrefix);
        //AND: A erroneous url
        String erroneousURL = "http://www.erroneousURL.com";

        //WHEN: The getArticleInfoFromDOI is called with the erroneous URL
        ArticleInfo output = elsevierArticleScrapper.getArticleInfoFromDOI(erroneousURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromURLWithStatusCodeDistinctTo200Test(){
        //GIVEN: The ElsevierArticleScrapper
        ElsevierArticleScrapper elsevierArticleScrapper = new ElsevierArticleScrapper(journalPrefix);
        //AND: A url that returns 404 Not Found error
        String url404 = "http://www.google.com/erroneousURL";

        //WHEN: The getArticleInfoFromDOI is called with the 404 error url
        ArticleInfo output = elsevierArticleScrapper.getArticleInfoFromDOI(url404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromCorrectURLTest(){
        //GIVEN: The ElsevierArticleScrapper
        ElsevierArticleScrapper elsevierArticleScrapper = new ElsevierArticleScrapper(journalPrefix);
        //AND: A correct url from Elsevier
        String correctElsevier = "https://doi.org/10.1016/j.ecoleng.2014.09.079";
        //AND: The expected output (an ArticleInfo object with the correct information)
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
        try {
            date = format.parse("December 2014");
        } catch (ParseException exception) {
            System.err.println(exception);
        }
        List<String> authors = Arrays.asList(new String[]{"Josu G. Alday", "Víctor M. Santana", "Rob H. Marrs", "Carolina Martínez-Ruiz"});
        ArticleInfo expectedOutput = new ArticleInfo("Shrub-induced understory vegetation changes in reclaimed mine sites","https://doi.org/10.1016/j.ecoleng.2014.09.079",
                authors,"Ecological Engineering","Volume 73, Pages 691-698",date,"December 2014");

        //WHEN: The getArticleInfoFromDOI is called with the correct Elsevier url
        ArticleInfo output = elsevierArticleScrapper.getArticleInfoFromDOI(correctElsevier);

        //THEN: The output must the same as the expectedOutput
        assertEquals(expectedOutput,output);
    }

}
