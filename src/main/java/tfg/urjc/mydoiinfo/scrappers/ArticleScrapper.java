package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import java.io.IOException;

public abstract class ArticleScrapper {

    private final String journalPrefix;

    public ArticleScrapper(String journalPrefix) {
        this.journalPrefix = journalPrefix;
    }

    public boolean isCorrectJournalScrapper(String DOI) {
        return DOI.contains("/"+journalPrefix+"/") || DOI.startsWith(journalPrefix);
    }

    protected int getHTTPStatusCode(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            System.out.println("Excepción al obtener el código de estado: " + ex.getMessage());
        }
        return response.statusCode();
    }

    public abstract void getArticleInfoFromDOI(String DOI);
}
