package tfg.urjc.mydoiinfo.scrappers;

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

public class ScienceArticleScrapper extends JSOUPArticleScrapper {

    public ScienceArticleScrapper(String[] journalPrefixList) {
        super(journalPrefixList);
    }

    public ArticleInfo getArticleInfoFromDOI(String DOI){

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(DOI);

            Element titleElement = document.select("h1[property=\"name\"]").first();
            String title = null;
            if(titleElement != null)
                title = titleElement.text();

            Elements authors = document.select("span[property=\"author\"] > a:not([property])");
            List<String> authorList = null;
            if(authors != null && authors.size()>0){
                if(authors.size()==1){
                    authorList = new ArrayList<>();
                    authorList.add(authors.first().attr("title"));
                }else {
                    authorList = authors.stream().map(elem->elem.text()).collect(Collectors.toList());
                }
            }

            Element elemJournal = document.select("span[property=\"name\"]").first();
            String journal = null;
            String volumeInfo = null;
            if(elemJournal != null){
                journal = elemJournal.text();
                Element elemVolume = document.select("span.core-enumeration").first();
                if(elemVolume != null)
                    volumeInfo = elemVolume.text();
            }

            Element dateElement = document.select("span[property=\"datePublished\"]").first();
            Date date = null;
            String dateString = null;
            if(dateElement != null){
                dateString = dateElement.text();
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.US);
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
