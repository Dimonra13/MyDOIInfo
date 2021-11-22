package tfg.urjc.mydoiinfo.scrappersTests.articleScrappersTests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.boot.test.context.SpringBootTest;
import tfg.urjc.mydoiinfo.scrappers.articleScrappers.IEEEArticleScrapper;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PhantomArticleScrapperTests {

    @Test
    public void getPhantomDriverTest(){
        //WHEN: The getPhantomDriver is called (using a subclass to access the protected method)
        PhantomJSDriver driver = new IEEEArticleScrapper(new String[]{"10.1109"}){
            public PhantomJSDriver getPhantomDriver(){
                return super.getPhantomDriver();
            }
        }.getPhantomDriver();

        //THEN: The driver must be distinct from null
        assertNotNull(driver);
    }
}
