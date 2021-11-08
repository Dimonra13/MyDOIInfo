package tfg.urjc.mydoiinfo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleInfoRepository  extends JpaRepository<ArticleInfo, Long> {

}