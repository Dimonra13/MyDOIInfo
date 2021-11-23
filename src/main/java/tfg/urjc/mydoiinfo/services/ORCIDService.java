package tfg.urjc.mydoiinfo.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Service
public class ORCIDService {

    //API DOCUMENTATION: https://pub.orcid.org/v3.0/#/
    private static final String BASE_ORCID_URL="https://pub.orcid.org/v3.0/";
    //https://pub.orcid.org/v2.0/0000-0002-9563-0691/works

    private JSONObject getJSONObjectFromORCIDid(String id){
        if (id == null)
            return null;
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(BASE_ORCID_URL+id+"/works");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response;
        try {
            response = invocationBuilder.get();
        } catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(response.readEntity(String.class));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
