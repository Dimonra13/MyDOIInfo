package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public abstract class JSOUPArticleScrapper extends ArticleScrapper {

    public JSOUPArticleScrapper(String journalPrefix) {
        super(journalPrefix);
    }

    protected Document getHtmlDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
        } catch (IOException ex) {
            System.out.println("Exception obtaining the web page: " + ex.getMessage());
        }
        return document;
    }
}
