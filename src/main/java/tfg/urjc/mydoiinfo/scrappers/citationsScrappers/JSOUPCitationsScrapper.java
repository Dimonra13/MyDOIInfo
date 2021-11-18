package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class JSOUPCitationsScrapper extends CitationsScrapper {

    protected Document getHtmlDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).get();
        } catch (Exception ex) {
            System.err.println("Exception obtaining the web page: " + ex.getMessage());
            ex.printStackTrace();
        }
        return document;
    }
}
