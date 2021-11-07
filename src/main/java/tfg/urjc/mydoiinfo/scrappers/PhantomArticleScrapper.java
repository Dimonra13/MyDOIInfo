package tfg.urjc.mydoiinfo.scrappers;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public abstract class PhantomArticleScrapper extends ArticleScrapper {

    public PhantomArticleScrapper(String[] journalPrefixList) {
        super(journalPrefixList);
    }

    protected PhantomJSDriver getPhantomDriver(){
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "./phantomjs-2.1.1-linux/phantomjs");
        return new PhantomJSDriver(caps);
    }
}
