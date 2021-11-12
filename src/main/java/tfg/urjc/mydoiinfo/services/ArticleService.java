package tfg.urjc.mydoiinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.domain.entities.JCRRegistry;
import tfg.urjc.mydoiinfo.domain.repositories.ArticleRepository;
import tfg.urjc.mydoiinfo.domain.repositories.JCRRegistryRepository;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleScrapperService articleScrapperService;

    @Autowired
    JCRRegistryRepository jcrRegistryRepository;

    public Article getArticleFromDOI(String doi){
        if(doi==null)
            return null;
        Article article = articleRepository.findFirstByDOI(doi);
        /*Scrap the article if it is not in the database (new article) or if it is in the database but its JCRResgistry
        is null (it may be that the JCR data for the corresponding year was not available at the time it was stored in
        the database but may be available now).
         */
        if(article==null || article.getJcrRegistry() == null){
            ArticleInfo articleInfo = articleScrapperService.getArticleInfoFromDOI(doi);
            if(articleInfo == null)
                return null;
            else {
                article = new Article(articleInfo);
                Integer year = (articleInfo.getPublicationYear() != null) ? articleInfo.getPublicationYear() : articleInfo.getPublicationDateYear();
                JCRRegistry jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year,articleInfo.getJournal());
                article.setJcrRegistry(jcrRegistry);
                article = articleRepository.save(article);
            }
        }
        return article;
    }

    public List<Article> getArticleFromDOIList(List<String> doiList){
        List<Article> output = new ArrayList<>();
        for(String doi: doiList){
            Article article = getArticleFromDOI(doi);
            if (article != null)
                output.add(article);
        }
        return output;
    }
}
