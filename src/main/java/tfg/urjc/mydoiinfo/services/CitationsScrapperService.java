package tfg.urjc.mydoiinfo.services;

import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.citationsScrappers.CitationsScrapper;
import tfg.urjc.mydoiinfo.scrappers.citationsScrappers.CrossRefApiCitationsScrapper;

@Service
public class CitationsScrapperService {

    private CitationsScrapper citationsScrapper = new CrossRefApiCitationsScrapper();

    public Long getCitationsFromArticle(Article article){
        if(article==null)
            return null;
        else
            return citationsScrapper.getCitationsFromArticle(article);
    }

    public Long getCitationsFromArticleInfo(ArticleInfo articleInfo){
        if (articleInfo==null)
            return null;
        else
            return getCitationsFromArticle(new Article(articleInfo));
    }
}
