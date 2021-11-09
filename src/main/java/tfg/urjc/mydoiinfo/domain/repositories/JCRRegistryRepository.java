package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.JCRRegistry;

@Repository
public interface JCRRegistryRepository extends JpaRepository<JCRRegistry, Long> {
    public JCRRegistry findByYearAndJournalNameIgnoreCase(Integer year,String journalName);
}