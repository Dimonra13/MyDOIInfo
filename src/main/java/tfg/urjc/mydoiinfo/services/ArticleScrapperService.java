package tfg.urjc.mydoiinfo.services;

import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;
import tfg.urjc.mydoiinfo.scrappers.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleScrapperService {

    private static final String[] acmPrefixList = new String[]{"10.1145"};
    private static final String[] ieeePrefixList = new String[]{"10.1109", "10.23919", "10.36227", "10.47962"};
    //Science Journal of the American Association for the Advancement of Science(AAAS)
    private static final String[] sciencePrefixList = new String[]{"10.1126"};
    private static final String[] elsevierPrefixList = new String[]{"10.1016", "10.2111", "10.1006", "10.1367", "10.1602",
            "10.2353", "10.1529", "10.7424", "10.3816", "10.3921", "10.1157", "10.1205", "10.3182", "10.4065", "10.1383",
            "10.1067", "10.1078", "10.1053", "10.1580", "10.1054", "10.1197", "10.1240"};
    private static final String[] springerPrefixList = new String[]{"10.1134", "10.1251", "10.1186", "10.4076", "10.1114",
            "10.1023", "10.5819", "10.2165", "10.1361", "10.1379", "10.1065", "10.1381", "10.7603", "10.1385", "10.3758",
            "10.5052", "10.1617", "10.1245", "10.4333", "10.1365", "10.1891", "10.1038", "10.1013", "10.3858", "10.7123",
            "10.33283", "10.1057", "10.1007", "10.1140", "10.26777", "10.26778"};

    private ArticleScrapper[] articleScrapperList = {
            new ACMArticleScrapper(acmPrefixList),
            new IEEEArticleScrapper(ieeePrefixList),
            new ScienceArticleScrapper(sciencePrefixList),
            new ElsevierArticleScrapper(elsevierPrefixList),
            new SpringerArticleScrapper(springerPrefixList)
    };

    public ArticleInfo getArticleInfoFromDOI(String DOI) {
        if (DOI == null)
            return null;
        for (ArticleScrapper scrapper : articleScrapperList) {
            if (scrapper.isCorrectJournalScrapper(DOI)) {
                return scrapper.getArticleInfoFromDOI(DOI);
            }
        }
        return null;
    }
}
