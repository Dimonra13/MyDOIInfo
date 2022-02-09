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
        //If the journal has " and " it is necessary to try replacing it with " & "
        if(jcrRegistry==null && articleInfo.getJournal()!=null && articleInfo.getJournal().contains(" and "))
            jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year,articleInfo.getJournal().replace(" and "," & "));
        //If the JCRRegistry is null, the previous year's JCRRegistry is searched (the current year's data are not available until the following year, so last year's data are used instead).
        if (jcrRegistry==null && year!=null){
            jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase((year-1),articleInfo.getJournal());
            //If the journal has " and " it is necessary to try replacing it with " & "
            if(jcrRegistry==null && articleInfo.getJournal()!=null && articleInfo.getJournal().contains(" and "))
                jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase((year-1),articleInfo.getJournal().replace(" and "," & "));
        }
        article.setJcrRegistry(jcrRegistry);
        return article;
    }

    public Article setJCRRegistry(Article article, String journalTitle, Integer year){
        if (article == null)
            return null;
        JCRRegistry jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year,journalTitle);
        //If the journal has " and " it is necessary to try replacing it with " & "
        if(jcrRegistry==null && journalTitle!=null && journalTitle.contains(" and "))
            jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year,journalTitle.replace(" and "," & "));
        //If the JCRRegistry is null, the previous year's JCRRegistry is searched (the current year's data are not available until the following year, so last year's data are used instead)
        if (jcrRegistry==null && year!=null){
            jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase((year-1),journalTitle);
            //If the journal has " and " it is necessary to try replacing it with " & "
            if(jcrRegistry==null && journalTitle!=null && journalTitle.contains(" and "))
                jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase((year-1),journalTitle.replace(" and "," & "));
        }
        article.setJcrRegistry(jcrRegistry);
        return article;
    }

}
