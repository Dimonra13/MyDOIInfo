package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.Conference;

import java.util.Date;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    public Conference findFirstByTitleIgnoreCase(String title);
    /*
    TODO: MAKE THIS QUERY WORKS
    select * from conference where param like concat('%',conference.title,'%');
    public Conference findFirstWhereConferenceTitleContains(String title);
     */
    public Conference findFirstByAcronymIgnoreCase(String acronym);
    public Conference findFirstByUpdatedDate(Date updateDate);
    public Conference findFirstByUpdatedDateAfter(Date updateDate);
}