package tfg.urjc.mydoiinfo.scrappers.articleScrappers;

import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.Scrapper;

import java.util.Arrays;

public abstract class ArticleScrapper extends Scrapper {

    private final String[] journalPrefixList;

    public ArticleScrapper(String[] journalPrefixList) {
        this.journalPrefixList = journalPrefixList;
    }

    public boolean isCorrectJournalScrapper(String DOI) {
        return Arrays.stream(journalPrefixList).anyMatch(journalPrefix -> (DOI.contains("/"+journalPrefix+"/") || DOI.startsWith(journalPrefix+"/")));
    }

    public abstract ArticleInfo getArticleInfoFromDOI(String DOI);
}
