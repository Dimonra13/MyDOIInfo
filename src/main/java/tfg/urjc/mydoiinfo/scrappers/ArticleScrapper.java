package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.Connection.*;
import org.jsoup.Jsoup;
import tfg.urjc.mydoiinfo.domain.ArticleInfo;

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
        } catch (Exception ex) {
            System.err.println("Exception obtaining the status code: " + ex.getMessage());
            return 400;
        }
        if(response!=null)
            return response.statusCode();
        else
            return 400;
    }

    public abstract ArticleInfo getArticleInfoFromDOI(String DOI);
}
