package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ElsevierArticleScrapper extends JSOUPArticleScrapper {

    public ElsevierArticleScrapper(String[] journalPrefixList) {
        super(journalPrefixList);
    }

    private String forgeRedirectUrl(String DOI){
        String redirectUrl = null;
        try {
            redirectUrl = Jsoup.connect(DOI).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute().url().toString();
        } catch (Exception ex) {
            System.err.println("Exception obtaining the status code: " + ex.getMessage());
            return null;
        }
        String[] splitedUrl = redirectUrl.split("/pii/");
        if (splitedUrl.length>1)
            return "https://www.sciencedirect.com/science/article/pii/"+splitedUrl[1];
        else
            return null;
    }

    public ArticleInfo getArticleInfoFromDOI(String DOI){

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {

            Document document = getHtmlDocument(forgeRedirectUrl(DOI));
            if(document == null)
                return null;

            Element titleElement = document.select("span.title-text").first();
            String title = null;
            if(titleElement != null)
                title = titleElement.text();

            Elements authors = document.select("a.author");
            List<String> authorList = null;
            if(authors != null && authors.size()>0){
                if(authors.size()==1){
                    authorList = new ArrayList<>();
                    authorList.add(authors.first().text());
                }else {
                    authorList = authors.stream().map(elem->elem.select(".given-name").text()+" "+elem.select(".surname").text()).collect(Collectors.toList());
                }
            }


            Element elemJournal = document.select("a.publication-title-link").first();
            String journal = null;
            if(elemJournal != null){
                journal = elemJournal.text();
            }


            Element elemMetadata = document.select("div.publication-volume > div.text-xs").first();
            String volumeInfo = null;
            Date date = null;
            String dateString = null;
            if(elemMetadata != null){
                String[] splitedMetadata = elemMetadata.text().split(", ");
                if (splitedMetadata.length==3){
                    volumeInfo = splitedMetadata[0]+", "+splitedMetadata[2].replace("Pages","pp");
                    dateString = splitedMetadata[1];
                } else if (splitedMetadata.length==4){
                    volumeInfo = splitedMetadata[0]+", "+splitedMetadata[1]+", "+splitedMetadata[3].replace("Pages","pp");
                    dateString = splitedMetadata[2];
                }
            }

            if(dateString != null){
                SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
                try {
                    date = format.parse(dateString);
                } catch (ParseException ex) {
                    System.err.println(ex);
                }
            }

            return new ArticleInfo(title,DOI,authorList,journal,volumeInfo,date,dateString);
        } else {
            System.err.println("ERROR: Status code is " + httpStatusCode);
            return null;
        }
    }
}
