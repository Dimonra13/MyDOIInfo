package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tfg.urjc.mydoiinfo.domain.entities.Article;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ScholarCitationsScrapper extends JSOUPCitationsScrapper{

    /*
    Important: Although the code present in this class works correctly, its use is not recommended since it may lead to
    the banning of the IP address. It is kept in the code as an example and model for the development of possible new
    JSOUPCitationScrappers.
     */
    private final String SCHOLAR_BASE_URL ="https://scholar.google.com/scholar?hl=es&q=";

    private String forgeEncodedUrl(Article article) {
        if(article.getTitle()==null)
            return null;
        String searchUrl = article.getTitle() + " ";
        if(article.getJournalTitle()!=null)
            searchUrl = searchUrl + article.getJournalTitle() + " ";
        if(article.getAuthors()!=null && article.getAuthors().size()>0)
            searchUrl = searchUrl + article.getAuthors().get(0) + " ";
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(searchUrl, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return SCHOLAR_BASE_URL + encodedUrl;
    }

    @Override
    public Long getCitationsFromArticle(Article article) {
        if(article == null)
            return null;
        String encodedUrl = forgeEncodedUrl(article);
        if (encodedUrl == null)
            return null;
        int httpStatusCode = getHTTPStatusCode(encodedUrl);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(encodedUrl);
            if(document==null){
                System.err.println("Error scrapping citations for article "+article.getTitle());
                return null;
            }
            Element searchResult = document.select("div.gs_r.gs_or.gs_scl").first();
            if(searchResult==null){
                return null;
            }else{
                Element titleElement = searchResult.select("h3.gs_rt").first();
                if(titleElement==null){
                    return null;
                } else {
                   String title = titleElement.text();
                   //Check that the element to scrap is the correct one
                   if(article.getTitle().equalsIgnoreCase(title) || article.getTitle().contains(title) || title.contains(article.getTitle())){
                        Element citationsElement = searchResult.select("a[href^=\"/scholar?cites\"] ").first();
                        if(citationsElement==null){
                            return null;
                        } else {
                            String citations = citationsElement.text().replace("Citado por ","");
                            Long citationNumber = null;
                            try {
                                citationNumber = Long.parseLong(citations);
                            }catch (Exception exception){
                                exception.printStackTrace();
                            }
                            return citationNumber;
                        }
                   } else {
                       return null;
                   }
                }
            }
        } else {
            System.err.println("ERROR: Status code is " + httpStatusCode + " scrapping citations for article "+article.getTitle());
            return null;
        }
    }

}
