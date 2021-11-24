package tfg.urjc.mydoiinfo.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.Article;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Service
public class ORCIDService {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleScrapperService articleScrapperService;

    @Autowired
    JCRRegistryService jcrRegistryService;

    //API DOCUMENTATION: https://pub.orcid.org/v3.0/#/
    private static final String BASE_ORCID_URL="https://pub.orcid.org/v3.0/";
    //https://pub.orcid.org/v3.0/0000-0002-9563-0691/works

    private JSONObject getJSONObjectFromORCIDid(String id){
        if (id == null)
            return null;
        //Create the client
        Client client = ClientBuilder.newClient();
        //Set the target URL and response type (JSON)
        WebTarget webTarget = client.target(BASE_ORCID_URL+id+"/works");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        //Perform the request and get the response
        Response response;
        try {
            response = invocationBuilder.get();
        } catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        //Check the response statusCode is 200 OK
        if (response == null)
            return null;
        if(response.getStatus() != 200){
            System.err.println("ERROR: Status code is " + response.getStatus() + " getting the list of articles of the person with ORCID id " + id);
            return null;
        }
        //Parse the response to a JSON Object
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) parser.parse(response.readEntity(String.class));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return jsonResponse;
    }

    public List<Article> getArticlesFromORCIDid(String id){
        //Get the response as a JSON Object
        JSONObject jsonResponse = getJSONObjectFromORCIDid(id);
        if(jsonResponse == null || jsonResponse.isEmpty())
            return null;
        List<Article> output = new ArrayList<>();
        //Get the array of JSON Objects which represents the different articles of the person with ORCID id $id
        JSONArray articleItemList;
        try {
            articleItemList = (JSONArray) jsonResponse.get("group");
        } catch (Exception e){
            e.printStackTrace();
            return output;
        }
        //For each ArticleItem the important data must be extracted and the corresponding services called to create
        //a correct Article Object.
        articleItemList.forEach( articleItem -> {
            //Get the summary item that contains all the information about the article
            JSONObject summary;
            try {
                summary = (JSONObject) ( (JSONArray) ( (JSONObject) articleItem ).get("work-summary") ).get(0);
            } catch (Exception exception){
                System.err.println("Error parsing summary from an article of the person with ORCID id: "+id);
                summary = null;
            }
            if (summary != null){
                //Get the extenalId object that contains the doi info
                JSONObject externalId = null;
                try {
                    JSONArray externalIdList = (JSONArray) ((JSONObject) summary.get("external-ids")).get("external-id");
                    if(externalIdList!=null && !externalIdList.isEmpty())
                        externalId = (JSONObject) externalIdList.get(0);
                }catch (Exception exception){
                    System.err.println("Error parsing article externalId from an article of the person with ORCID id: "+id);
                    externalId = null;
                }
                String articleDOI = null;
                if(externalId != null){
                    //Get the DOI info if the external-id-type is really doi
                    try {
                        String type = (String) externalId.get("external-id-type");
                        if (type.equals("doi")){
                            articleDOI = (String) ((JSONObject) externalId.get("external-id-normalized")).get("value");
                        }
                    } catch (Exception e){
                        System.err.println("Error parsing article doi from an article of the person with ORCID id: "+id);
                        articleDOI = null;
                    }
                }
                //If the doi is not null and there is a scrapper capable of obtaining the article information
                //the ArticleService is call to create and save the article object
                if(articleDOI!=null && articleScrapperService.existsArticleScrapperForDOI(articleDOI)){
                    Article article = articleService.getArticleFromDOI("https://doi.org/"+articleDOI);
                    if(article!=null)
                        output.add(article);
                } else {
                    //If the doi is null or there is no scrapper capable of gathering the information, the data from ORCID
                    //is use to create the best posible article object without scrapping
                    String articleTitle;
                    try {
                        articleTitle = (String) ((JSONObject) ((JSONObject) summary.get("title")).get("title")).get("value");
                    } catch (Exception exception){
                        System.err.println("Error parsing article title from an article of the person with ORCID id: "+id);
                        articleTitle = null;
                    }
                    String type;
                    try {
                        type = (String) summary.get("type");
                    } catch (Exception exception){
                        System.err.println("Error parsing type from an article of the person with ORCID id: "+id);
                        type = null;
                    }
                    String journalTitle;
                    try {
                        journalTitle = (String) ((JSONObject) summary.get("journal-title")).get("value");
                    } catch (Exception exception){
                        System.err.println("Error parsing journal title from an article of the person with ORCID id: "+id);
                        journalTitle = null;
                    }
                    //TODO: FINSIH THE DATA GATHERING
                    //TODO: Create the article object but don't save it in the database

                }
            }
        });
        return output;
    }
}
