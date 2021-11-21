package tfg.urjc.mydoiinfo.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tfg.urjc.mydoiinfo.domain.entities.Conference;

import java.util.Date;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    public Conference findFirstByTitleIgnoreCase(String title);
    public Conference findFirstByUpdatedDate(Date updateDate);
    public Conference findFirstByUpdatedDateAfter(Date updateDate);
}