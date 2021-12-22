package tfg.urjc.mydoiinfo.controllers.api;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.domain.jsonViews.CompleteArticle;
import tfg.urjc.mydoiinfo.services.ArticleService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/doi")
public class DOIController {

    @Autowired
    ArticleService articleService;

    private final int MAX_INPUT_DOI_SET_SIZE = 300;

    @GetMapping("/")
    @JsonView(CompleteArticle.class)
    public ResponseEntity<Set<Article>> getArticlesFromDOIList (@RequestParam Set<String> dois){
        if (dois==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (dois.isEmpty())
            return new ResponseEntity<>(new HashSet<>(),HttpStatus.NOT_FOUND);
        //The size of the input set of DOIs is limited to protect the system against possible attacks and to avoid
        //excessive response delays
        if (dois.size()> MAX_INPUT_DOI_SET_SIZE) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Transform input set to list
        List<String> doiList;
        try{
            doiList = new ArrayList<>(dois);
        } catch (Exception e){
            //If an error occurred during the conversion return status code 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Get the scientific articles that correlate with the DOIs in the set received in the request
        List<Article> output = articleService.getArticlesFromDOIList(doiList);
        ResponseEntity<Set<Article>> responseEntity;
        if (output==null) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            try {
                //Convert the list to a set to eliminate possible duplicates
                responseEntity = new ResponseEntity<>(new HashSet<>(output),HttpStatus.OK);
            } catch (Exception e){
                //If an error occurred during the conversion return status code 500
                responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return responseEntity;
    }

    /*
    To avoid possible attacks it is not allowed to have // in the url, therefore to use this endpoint it is necessary
    to put the doi with the prefix/suffix format. The above endpoint does allow processing DOIs in url format as it is
    passed as a parameter instead of being part of the url itself.
     */
    @GetMapping("/**")
    @JsonView(CompleteArticle.class)
    public ResponseEntity<Article> getArticleFromDOI (HttpServletRequest request){
        //The DOI included in the request URL is obtained
        String doi = request.getRequestURL().toString().split("/api/")[1].replace("doi/","");
        Article output = articleService.getArticleFromUserInputDOI(doi);
        if (output==null)
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(output,HttpStatus.OK);
    }

}
