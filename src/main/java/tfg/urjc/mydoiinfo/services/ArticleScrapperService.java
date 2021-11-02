package tfg.urjc.mydoiinfo.services;

import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.ACMArticleScrapper;
import tfg.urjc.mydoiinfo.scrappers.ArticleScrapper;
import tfg.urjc.mydoiinfo.scrappers.IEEEArticleScrapper;
import tfg.urjc.mydoiinfo.scrappers.ScienceArticleScrapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleScrapperService {

    private static ArticleScrapper[] articleScrapperList = {
            new ACMArticleScrapper("10.1145"),
            new IEEEArticleScrapper("10.1109"),
            new ScienceArticleScrapper("10.1126")
    };

    public List<ArticleInfo> getArticleInfoFromDOIList(List<String> DOIs) {
        List<ArticleInfo> out = new ArrayList<>();
        for (String DOI : DOIs) {
            for (ArticleScrapper scrapper : articleScrapperList) {
                if (scrapper.isCorrectJournalScrapper(DOI)) {
                    out.add(scrapper.getArticleInfoFromDOI(DOI));
                    break;
                }
            }
        }
        return out;
    }
}
