package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.CategoryRanking;

@Repository
public interface CategoryRankingRepository extends JpaRepository<CategoryRanking, Long> {

}