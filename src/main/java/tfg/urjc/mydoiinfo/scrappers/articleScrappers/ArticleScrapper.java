package tfg.urjc.mydoiinfo.scrappers.articleScrappers;

import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.Scrapper;

public abstract class ArticleScrapper extends Scrapper {

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

    public abstract ArticleInfo getArticleInfoFromDOI(String DOI);
}
