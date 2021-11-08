package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.Connection.*;
import org.jsoup.Jsoup;

public abstract class ArticleScrapper {

    private final String[] journalPrefixList;

    public ArticleScrapper(String[] journalPrefixList) {
        this.journalPrefixList = journalPrefixList;
    }

    public boolean isCorrectJournalScrapper(String DOI) {
        for (String journalPrefix:journalPrefixList){
            if(DOI.contains("/"+journalPrefix+"/") || DOI.startsWith(journalPrefix))
                return true;
        }
        return false;
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
