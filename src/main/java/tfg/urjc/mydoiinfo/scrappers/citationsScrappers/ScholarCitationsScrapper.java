package tfg.urjc.mydoiinfo.scrappers.citationsScrappers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ScholarCitationsScrapper extends JSOUPCitationsScrapper{

    private final String scholarBaseUrl="https://scholar.google.com/scholar?hl=es&q=";

    private String forgeEncodedUrl(ArticleInfo articleInfo) {
        if(articleInfo.getTitle()==null)
            return null;
        String searchUrl = articleInfo.getTitle() + " ";
        if(articleInfo.getJournal()!=null)
            searchUrl = searchUrl + articleInfo.getJournal() + " ";
        if(articleInfo.getAuthors()!=null && articleInfo.getAuthors().size()>0)
            searchUrl = searchUrl + articleInfo.getAuthors().get(0) + " ";
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(searchUrl, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return scholarBaseUrl + encodedUrl;
    }

    @Override
    public Integer getCitationsFromArticleInfo(ArticleInfo articleInfo) {
        if(articleInfo == null)
            return null;
        String encodedUrl = forgeEncodedUrl(articleInfo);
        if (encodedUrl == null)
            return null;
        int httpStatusCode = getHTTPStatusCode(encodedUrl);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(encodedUrl);
            if(document==null){
                System.err.println("Error scrapping citations for article "+articleInfo.getTitle());
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
                   if(articleInfo.getTitle().equalsIgnoreCase(title) || articleInfo.getTitle().contains(title) || title.contains(articleInfo.getTitle())){
                        Element citationsElement = searchResult.select("a[href^=\"/scholar?cites\"] ").first();
                        if(citationsElement==null){
                            return null;
                        } else {
                            String citations = citationsElement.text().replace("Citado por ","");
                            Integer citationNumber = null;
                            try {
                                citationNumber = Integer.parseInt(citations);
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
            System.err.println("ERROR: Status code is " + httpStatusCode + " scrapping citations for article "+articleInfo.getTitle());
            return null;
        }
    }

}
