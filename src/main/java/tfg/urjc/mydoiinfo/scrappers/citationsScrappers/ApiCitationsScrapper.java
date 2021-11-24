package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public abstract class ApiCitationsScrapper extends CitationsScrapper{

    protected Response getResponseFromURL(String url){
        if (url == null)
            return null;
        //Create the client
        Client client = ClientBuilder.newClient();
        //Set the target URL and response type (JSON)
        WebTarget webTarget = client.target(url);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        //Perform the request and get the response
        Response response;
        try {
            response = invocationBuilder.get();
        } catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        return response;
    }
}
