package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.Conference;

import java.util.Date;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    public Conference findFirstByTitleIgnoreCase(String title);
    @Query(
            value = "SELECT * FROM conference c WHERE ?1 LIKE CONCAT('%',c.title,'%') LIMIT 1",
            nativeQuery = true
    )
    public Conference findFirstWhereConferenceTitleIsContainedIn(String title);
    public Conference findFirstByAcronymIgnoreCase(String acronym);
    public Conference findFirstByUpdatedDate(Date updateDate);
    public Conference findFirstByUpdatedDateAfter(Date updateDate);
}