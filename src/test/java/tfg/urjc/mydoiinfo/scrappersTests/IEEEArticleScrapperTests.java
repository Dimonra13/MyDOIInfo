package tfg.urjc.mydoiinfo.scrappersTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.IEEEArticleScrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class IEEEArticleScrapperTests {

    private String[] journalPrefixList = new String[]{"10.1109"};

    @Test
    public void getArticleInfoFromNullURLTest(){
        //GIVEN: The IEEEArticleScrapper
        IEEEArticleScrapper ieeeArticleScrapper = new IEEEArticleScrapper(journalPrefixList);
        //AND: A null url
        String nullURL = null;

        //WHEN: The getArticleInfoFromDOI is called with the null URL
        ArticleInfo output = ieeeArticleScrapper.getArticleInfoFromDOI(nullURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromMalformedURLTest(){
        //GIVEN: The IEEEArticleScrapper
        IEEEArticleScrapper ieeeArticleScrapper = new IEEEArticleScrapper(journalPrefixList);
        //AND: A fake url
        String malformedURL = "malformed";

        //WHEN: The getArticleInfoFromDOI is called with the malformed URL
        ArticleInfo output = ieeeArticleScrapper.getArticleInfoFromDOI(malformedURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromErroneousURLTest(){
        //GIVEN: The IEEEArticleScrapper
        IEEEArticleScrapper ieeeArticleScrapper = new IEEEArticleScrapper(journalPrefixList);
        //AND: A erroneous url
        String erroneousURL = "http://www.erroneousURL.com";

        //WHEN: The getArticleInfoFromDOI is called with the erroneous URL
        ArticleInfo output = ieeeArticleScrapper.getArticleInfoFromDOI(erroneousURL);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromURLWithStatusCodeDistinctTo200Test(){
        //GIVEN: The IEEEArticleScrapper
        IEEEArticleScrapper ieeeArticleScrapper = new IEEEArticleScrapper(journalPrefixList);
        //AND: A url that returns 404 Not Found error
        String url404 = "http://www.google.com/erroneousURL";

        //WHEN: The getArticleInfoFromDOI is called with the 404 error url
        ArticleInfo output = ieeeArticleScrapper.getArticleInfoFromDOI(url404);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getArticleInfoFromCorrectURLTest(){
        //GIVEN: The IEEEArticleScrapper
        IEEEArticleScrapper ieeeArticleScrapper = new IEEEArticleScrapper(journalPrefixList);
        //AND: A correct url from IEEE
        String correctIEEE = "https://doi.org/10.1109/LCA.2021.3081752";
        //AND: The expected output (an ArticleInfo object with the correct information)
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        try {
            date = format.parse("19 May 2021");
        } catch (ParseException exception) {
            System.err.println(exception);
        }
        List<String> authors = Arrays.asList(new String[]{"Yongjoo Jang", "Sejin Kim", "Daehoon Kim", "Sungjin Lee", "Jaeha Kung"});
        ArticleInfo expectedOutput = new ArticleInfo("Deep Partitioned Training From Near-Storage Computing to DNN Accelerators","https://doi.org/10.1109/LCA.2021.3081752",
                authors,"IEEE Computer Architecture Letters","Volume: 20, Issue: 1, pp 70 - 73",date,"19 May 2021");

        //WHEN: The getArticleInfoFromDOI is called with the correct IEEE url
        ArticleInfo output = ieeeArticleScrapper.getArticleInfoFromDOI(correctIEEE);

        //THEN: The output must the same as the expectedOutput
        assertEquals(expectedOutput,output);
    }

}
