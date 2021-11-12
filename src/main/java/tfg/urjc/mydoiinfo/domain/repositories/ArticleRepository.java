package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    public Article findFirstByDOI(String doi);
}