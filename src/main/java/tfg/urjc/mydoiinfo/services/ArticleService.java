package tfg.urjc.mydoiinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    JCRRegistryService jcrRegistryService;

    @Autowired
    CitationsScrapperService citationsScrapperService;

    @Autowired
    ConferenceService conferenceService;

    @Transactional
    public Article updateCitationsForArticle(Article article){
        if(article==null)
            return null;
        else {
            Long citations = citationsScrapperService.getCitationsFromArticle(article);
            if(citations!=null){
                article.setCitations(citations);
                return articleRepository.save(article);
            } else {
                /*
                If citations is null, the article is not updated. This is because, in case the citations of the
                original article were null, it is not necessary to reassign null; and in case the citations were
                other than null, it is preferable to keep an old real value than a null value that may be due to
                scrapping errors.
                 */
                return article;
            }
        }

    }


    private Article updateArticleFromArticleInfo(Article article, ArticleInfo articleInfo){
        if (article == null)
            return null;
        if (articleInfo == null)
            return article;

        if(articleInfo.getTitle()!=null)
            article.setTitle(articleInfo.getTitle());
        if(articleInfo.getDOI()!=null)
            article.setDOI(articleInfo.getDOI());
        if(articleInfo.getAuthors()!=null)
            article.setAuthors(articleInfo.getAuthors());
        if(articleInfo.getVolumeInfo()!=null)
            article.setVolumeInfo(articleInfo.getVolumeInfo());
        if(articleInfo.getPublicationDate()!=null)
            article.setPublicationDate(articleInfo.getPublicationDate());
        if(articleInfo.getPublicationDateText()!=null)
            article.setPublicationDateText(articleInfo.getPublicationDateText());
        if(articleInfo.getJournal()!=null)
            article.setJournalTitle(articleInfo.getJournal());
        if(articleInfo.getConferenceAcronym()!=null)
            article.setConferenceAcronym(articleInfo.getConferenceAcronym());

        return articleRepository.save(article);

    }

    @Transactional
    public Article getArticleFromDOI(String doi){
        if(doi==null)
            return null;
        Article article = articleRepository.findFirstByDOI(doi);
        /*
        Scrap the article if it is not in the database (new article) or if it is in the database but its JCRResgistry
        and conference are null (it may be that the JCR data for the corresponding year or the conference data was not
        available at the time it was stored in the database but may be available now).
         */
        if(article==null || (article.getJcrRegistry() == null && article.getConference() == null)){
            ArticleInfo articleInfo = articleScrapperService.getArticleInfoFromDOI(doi);
            if(articleInfo == null){
                if(article!=null){
                    article = updateCitationsForArticle(article);
                    return article;
                } else {
                    return null;
                }
            } else {
                //If the article is null, create a new one
                if(article==null){
                    article = new Article(articleInfo);
                } else {
                    article = updateArticleFromArticleInfo(article,articleInfo);
                }
                //Scrap the citations for the article
                Long citations = citationsScrapperService.getCitationsFromArticleInfo(articleInfo);
                //If the scrapped citations are null but the article is not null, then the original article citations
                //are used for the updated article
                if(citations==null && article!=null)
                    citations=article.getCitations();
                article.setCitations(citations);
                //Find the JCRRegistry for the article
                article = jcrRegistryService.setJCRRegistry(article,articleInfo);
                if(article.getJcrRegistry()==null)
                    article.setConference(conferenceService.getConference(article.getConferenceAcronym(),article.getJournalTitle()));
                //Save the article
                article = articleRepository.save(article);
            }
        } else {
            //Even if the article is already in the database, it is necessary to scrap the number of citations to keep it up to date
            article = updateCitationsForArticle(article);
        }
        return article;
    }

    public List<Article> getArticlesFromDOIList(List<String> doiList){
        List<Article> output = new ArrayList<>();
        if(doiList!=null){
            for(String doi: doiList){
                Article article = getArticleFromDOI(doi);
                if (article != null)
                    output.add(article);
            }
        }
        return output;
    }
}
