package tfg.urjc.mydoiinfo.domain.jsonViews;

import tfg.urjc.mydoiinfo.domain.entities.*;

public interface CompleteArticle extends
        Article.ArticleBasicData, Article.ArticleRelatedData,
        JCRRegistry.JCRBasicData, JCRRegistry.JCRRelatedData,
        CategoryRanking.CategoryRankingData,
        Journal.JournalBasicData,
        Conference.ConferenceData {}
