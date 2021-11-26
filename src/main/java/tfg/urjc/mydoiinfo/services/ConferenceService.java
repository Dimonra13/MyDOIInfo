package tfg.urjc.mydoiinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.Conference;
import tfg.urjc.mydoiinfo.domain.repositories.ConferenceRepository;

@Service
public class ConferenceService {

    @Autowired
    ConferenceRepository conferenceRepository;

    public Conference getConference(String acronym, String title){
        Conference conference = null;
        //If the title contains journal or magazine it is not a conference
        if (title!=null && (title.toUpperCase().contains("JOURNAL") || title.toUpperCase().contains("MAGAZINE")))
            return null;
        //The conference is searched by the acronym (only if it is not null) and if the conference is found
        //it is returned
        if(acronym!=null){
            conference = conferenceRepository.findFirstByAcronymIgnoreCase(acronym);
        }
        if (conference!=null)
            return conference;

        //If the conference has not been found, it is searched using the title (if it is not null)
        if (title!=null){
            conference = conferenceRepository.findFirstWhereConferenceTitleIsContainedIn(title);
        }
        return conference;
    }
}
