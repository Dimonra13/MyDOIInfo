package tfg.urjc.mydoiinfo.servicesTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import tfg.urjc.mydoiinfo.domain.entities.Conference;
import tfg.urjc.mydoiinfo.domain.repositories.ConferenceRepository;
import tfg.urjc.mydoiinfo.services.ConferenceService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConferenceServiceTests {

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    ConferenceService conferenceService;

    @BeforeAll
    void setUp(){
        Conference conference = new Conference("ACM INTERNATIONAL CONFERENCE ON INTERACTIVE EXPERIENCES FOR TV AND ONLINE VIDEO","VIDEO",null,null,null,null);
        conferenceRepository.save(conference);
    }

    @Test
    public void getConferenceNullAcronymAndNullTitleTest(){
        //GIVEN: A null acronym and a null title
        String acronym = null;
        String title =null;

        //WHEN: The getConference method of the service is called with the specified acronym and title
        Conference output = conferenceService.getConference(acronym,title);

        //THEN: The output must be null
        assertNull(output);
    }

    @Test
    public void getConferenceCorrectAcronymTest(){
        //GIVEN: A correct acronym and a title
        String acronym = "Video";
        String title = null;

        //WHEN: The getConference method of the service is called with the specified acronym and title
        Conference output = conferenceService.getConference(acronym,title);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output acronym must be the same as the specified acronym
        assertTrue(acronym.equalsIgnoreCase(output.getAcronym()));
    }

    @Test
    public void getConferenceNullAcronymAndCorrectTitleTest(){
        //GIVEN: A null acronym and a correct title
        String acronym = null;
        String title = "Proceedings of the ACM International Conference on Interactive Experiences for TV and Online Video";

        //WHEN: The getConference method of the service is called with the specified acronym and title
        Conference output = conferenceService.getConference(acronym,title);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output title must be contained or equals to the specified title
        assertTrue(title.toUpperCase().contains(output.getTitle().toUpperCase()));
    }

    @Test
    public void getConferenceIncorrectAcronymAndCorrectTitleTest(){
        //GIVEN: An incorrect acronym and a correct title
        String acronym = "wrong";
        String title = "Proceedings of the ACM International Conference on Interactive Experiences for TV and Online Video";

        //WHEN: The getConference method of the service is called with the specified acronym and title
        Conference output = conferenceService.getConference(acronym,title);

        //THEN: The output must be not null
        assertNotNull(output);
        //AND: The output title must be contained or equals to the specified title
        assertTrue(title.toUpperCase().contains(output.getTitle().toUpperCase()));
    }

    @Test
    public void getConferenceIncorrectAcronymAndIncorrectTitleTest(){
        //GIVEN: An incorrect acronym and incorrect title
        String acronym = "wrong";
        String title = "wrong";

        //WHEN: The getConference method of the service is called with the specified acronym and title
        Conference output = conferenceService.getConference(acronym,title);

        //THEN: The output must be null
        assertNull(output);
    }
}
