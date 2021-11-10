package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.Journal;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

    public Journal findFirstByTitleIgnoreCase(String title);
}