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
import tfg.urjc.mydoiinfo.services.ArticleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/api/doi")
public class DOIController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/")
    @JsonView(CompleteArticle.class)
    public ResponseEntity<Set<Article>> getArticlesFromDOIList (HttpServletRequest request){
        //TODO
        return new ResponseEntity<>(HttpStatus.OK);
    }

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
