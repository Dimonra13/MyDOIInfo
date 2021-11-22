package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class Scrapper {

    protected int getHTTPStatusCode(String url) {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).ignoreHttpErrors(true).execute();
        } catch (Exception ex) {
            System.err.println("Exception obtaining the status code: " + ex.getMessage());
            ex.printStackTrace();
            return 400;
        }
        if(response!=null)
            return response.statusCode();
        else
            return 400;
    }
}
