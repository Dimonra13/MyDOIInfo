package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tfg.urjc.mydoiinfo.domain.entities.Article;

import javax.ws.rs.core.Response;


public class CrossRefApiCitationsScrapper extends ApiCitationsScrapper{

    private final String crossRefBaseUrl="http://api.crossref.org/works/";

    @Override
    public Long getCitationsFromArticle(Article article) {
        if(article==null || article.getDOI()==null)
            return null;
        Response response = getResponseFromURL(crossRefBaseUrl+article.getDOI());
        if(response == null)
            return null;
        Integer statusCode = response.getStatus();
        if(statusCode == 200){
            String stringToParse = response.readEntity(String.class);
            JSONParser parser = new JSONParser();
            JSONObject json;
            try {
                json = (JSONObject) parser.parse(stringToParse);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            Long citations;
            try{
                citations = (Long) ((JSONObject) json.get("message")).get("is-referenced-by-count");
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return citations;
        } else {
            System.err.println("ERROR: Status code is " + statusCode + " scrapping citations for article "+article.getTitle());
            return null;
        }
    }
}
