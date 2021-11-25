package tfg.urjc.mydoiinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.domain.entities.JCRRegistry;
import tfg.urjc.mydoiinfo.domain.repositories.JCRRegistryRepository;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

@Service
public class JCRRegistryService {

    @Autowired
    JCRRegistryRepository jcrRegistryRepository;

    public Article setJCRRegistry(Article article, ArticleInfo articleInfo){
        if (article == null)
            return null;
        if(articleInfo == null)
            return article;
        Integer year = (articleInfo.getPublicationYear() != null) ? articleInfo.getPublicationYear() : articleInfo.getPublicationDateYear();
        JCRRegistry jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year,articleInfo.getJournal());
        article.setJcrRegistry(jcrRegistry);
        return article;
    }

    public Article setJCRRegistry(Article article, String journalTitle, Integer year){
        if (article == null)
            return null;
        JCRRegistry jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year,journalTitle);
        article.setJcrRegistry(jcrRegistry);
        return article;
    }

}
