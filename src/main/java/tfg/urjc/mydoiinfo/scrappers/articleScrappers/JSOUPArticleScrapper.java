package tfg.urjc.mydoiinfo.scrappers.articleScrappers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class JSOUPArticleScrapper extends ArticleScrapper {

    public JSOUPArticleScrapper(String[] journalPrefixList) {
        super(journalPrefixList);
    }

    protected Document getHtmlDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();
        } catch (Exception ex) {
            System.err.println("Exception obtaining the web page: " + ex.getMessage());
            ex.printStackTrace();
        }
        return document;
    }
}
