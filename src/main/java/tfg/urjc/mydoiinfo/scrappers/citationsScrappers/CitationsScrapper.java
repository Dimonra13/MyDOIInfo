package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.scrappers.Scrapper;

public abstract class CitationsScrapper extends Scrapper {
    public abstract Long getCitationsFromArticle(Article article);
}
