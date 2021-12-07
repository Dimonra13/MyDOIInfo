package tfg.urjc.mydoiinfo.controllers.api;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.domain.jsonViews.CompleteArticle;
import tfg.urjc.mydoiinfo.services.ORCIDService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orcid")
public class ORCIDController {

    @Autowired
    ORCIDService orcidService;

    @GetMapping("/{id}")
    @JsonView(CompleteArticle.class)
    public ResponseEntity<Set<Article>> getArticleFromORCIDid(@PathVariable String id){
        //Check if the id really is a valid ORCID id with the correct format
        if(id.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")){
            //Get the list of article belonging to the person with the specified ORCID id
            List<Article> output = orcidService.getArticlesFromORCIDid(id);
            if (output==null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                ResponseEntity<Set<Article>> responseEntity;
                try {
                    //Convert the list to a set to eliminate possible duplicates
                    responseEntity = new ResponseEntity<>(new HashSet<>(output),HttpStatus.OK);
                } catch (Exception e){
                    //If an error occurred during the conversion return status code 500
                    responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return responseEntity;
            }
        } else {
            //If the parameter is not an ORCID id return status code 400
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
