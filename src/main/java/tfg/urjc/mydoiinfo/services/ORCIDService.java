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

    @Autowired
    ConferenceService conferenceService;

    @Autowired
    CitationsScrapperService citationsScrapperService;

    //API DOCUMENTATION: https://pub.orcid.org/v3.0/#/
    private static final String BASE_ORCID_URL="https://pub.orcid.org/v3.0/";

    private JSONObject getJSONObjectFromORCIDid(String id,String endpoint){
        if (id == null)
            return null;
        //Create the client
        Client client = ClientBuilder.newClient();
        //Set the target URL and response type (JSON)
        WebTarget webTarget = client.target(BASE_ORCID_URL+id+"/"+endpoint);
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
            System.err.println("ERROR: Status code is " + response.getStatus() + " in the request to " + BASE_ORCID_URL+id+"/"+endpoint);
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

    public String getAuthorNameFromORCIDId(String id){
        String authorName = null;
        //Get the response body as a JSONObject
        JSONObject jsonResponsePerson = getJSONObjectFromORCIDid(id,"person");
        if(jsonResponsePerson == null || jsonResponsePerson.isEmpty()){
            return null;
        } else {
            //Get the name information from the response
            JSONObject nameInfo;
            try {
                nameInfo = (JSONObject) jsonResponsePerson.get("name");
            } catch (Exception exception){
                System.err.println("Error parsing name for author with ORCID id "+id);
                return null;
            }
            //If nameInfo is null it means that there is no information about the name of the author so the process terminates
            if (nameInfo == null || nameInfo.isEmpty())
                return null;

            //If the author has a credit name, return this name
            String creditName;
            try {
                    creditName = (String) ((JSONObject) nameInfo.get("credit-name")).get("value");
            }catch (Exception e){
                    creditName = null;
            }
            if (creditName!=null && !creditName.equals("")){
                return creditName;
            }

            //If the author doesn't have a credit name, return the given-name and family-name
            String givenName;
            try {
                givenName = (String) ((JSONObject) nameInfo.get("given-names")).get("value");
            }catch (Exception e){
                givenName = null;
            }
            String familyName;
            try {
                familyName = (String) ((JSONObject) nameInfo.get("family-name")).get("value");
            }catch (Exception e){
                familyName = null;
            }
            return ((givenName != null) ? ((familyName!=null) ? givenName+" "+familyName : givenName) : ((familyName!=null) ? familyName : null));
        }
    }

    public List<Article> getArticlesFromORCIDid(String id){
        //Get the author name
        String authorName = getAuthorNameFromORCIDId(id);
        //Get the response as a JSON Object
        JSONObject jsonResponseWorks = getJSONObjectFromORCIDid(id,"works");
        if(jsonResponseWorks == null || jsonResponseWorks.isEmpty())
            return null;
        List<Article> output = new ArrayList<>();
        //Get the array of JSON Objects which represents the different articles of the person with ORCID id $id
        JSONArray articleItemList;
        try {
            articleItemList = (JSONArray) jsonResponseWorks.get("group");
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
            //If summary is not null the article information can be process
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
                    //is used to create the best possible article object without scrapping
                    //Get the article title
                    String articleTitle;
                    try {
                        articleTitle = (String) ((JSONObject) ((JSONObject) summary.get("title")).get("title")).get("value");
                    } catch (Exception exception){
                        System.err.println("Error parsing article title from an article of the person with ORCID id: "+id);
                        articleTitle = null;
                    }
                    //If the article title cannot be read the item is skipped
                    if (articleTitle != null){
                        //Get the publication Year
                        String publicationDate;
                        try{
                            JSONObject year = (JSONObject) ((JSONObject) summary.get("publication-date")).get("year");
                            publicationDate = (year != null) ? (String) year.get("value") : null;
                        } catch (Exception exception){
                            System.err.println("Error parsing article publication date from an article of the person with ORCID id: "+id);
                            publicationDate = null;
                        }
                        //If the authorName was read correctly it is used to create article object
                        List<String> authorList = new ArrayList<>();
                        if (authorName!=null && !authorName.equals("")){
                            authorList.add(authorName);
                        }
                        //Create the article object but don't save it in the database
                        Article article = new Article(articleTitle,articleDOI,authorList,null,null,null,publicationDate,null);

                        //If the article has a DOI, try to get the article's citations
                        if(article.getDOI()!=null){
                            article.setDOI("https://doi.org/"+article.getDOI());
                            article.setCitations(citationsScrapperService.getCitationsFromArticle(article));
                        }
                        //Get the journal title
                        String journalTitle;
                        try {
                            journalTitle = (String) ((JSONObject) summary.get("journal-title")).get("value");
                        } catch (Exception exception){
                            System.err.println("Error parsing journal title from an article of the person with ORCID id: "+id);
                            journalTitle = null;
                        }
                        if(journalTitle!=null)
                            article.setJournalTitle(journalTitle);

                        //Get the publication type (journal, conference, magazine, ...)
                        String type;
                        try {
                            type = (String) summary.get("type");
                        } catch (Exception exception){
                            System.err.println("Error parsing type from an article of the person with ORCID id: "+id);
                            type = null;
                        }
                        //If type is conference-paper try to find the conference in the database
                        if(type != null && type.equals("conference-paper")){
                            article.setConference(conferenceService.getConference(null,journalTitle));
                            //If the type is journal-article and the publication year is not null try to get the JCRRegistry
                        } else if (type != null && type.equals("journal-article") && publicationDate != null){
                            Integer year;
                            try {
                                year = Integer.parseInt(publicationDate);
                            } catch (Exception exception){
                                year = null;
                            }
                            article = jcrRegistryService.setJCRRegistry(article,journalTitle,year);
                        }
                        //Add the article object created to the output list
                        output.add(article);
                    }
                }
            }
        });
        return output;
    }
}
