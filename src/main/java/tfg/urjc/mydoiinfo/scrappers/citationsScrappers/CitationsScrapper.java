package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.Scrapper;

public abstract class CitationsScrapper extends Scrapper {
    public abstract Integer getCitationsFromArticleInfo(ArticleInfo articleInfo);
}
