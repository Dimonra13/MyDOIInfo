package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tfg.urjc.mydoiinfo.domain.ArticleInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ACMArticleScrapper extends JSOUPArticleScrapper {

    public ACMArticleScrapper(String journalPrefix) {
        super(journalPrefix);
    }

    public ArticleInfo getArticleInfoFromDOI(String DOI){

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(DOI);

            Element titleElement = document.select("h1.citation__title").first();
            String title = null;
            if(titleElement != null)
                title = titleElement.text();

            Elements authors = document.select("a.author-name");
            List<String> authorList = null;
            if(authors != null && authors.size()>0){
                if(authors.size()==1){
                    authorList = new ArrayList<>();
                    authorList.add(authors.first().attr("title"));
                }else {
                    authorList = authors.stream().map(elem->elem.attr("title")).collect(Collectors.toList());
                }
            }


            Element elemJournal = document.select("span.epub-section__title").first();
            String journal = null;
            String volumeInfo = null;
            if(elemJournal != null){
                journal = elemJournal.text();
                Element elemVolume = document.select("span.comma-separator").first();
                if(elemVolume != null)
                    volumeInfo = elemVolume.text();
                Element elemPages = document.select("span.epub-section__pagerange").first();
                if(elemPages != null)
                    volumeInfo = volumeInfo + ", " + elemPages.text();
            }

            Element dateElement = document.select("span.CitationCoverDate").first();
            Date date = null;
            String dateString = null;
            if(dateElement != null){
                dateString = dateElement.text();
                SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.US);
                try {
                    date = format.parse(dateString);
                } catch (ParseException ex) {
                    SimpleDateFormat otherFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                    try {
                        date = otherFormat.parse(dateString);
                    } catch (ParseException exception) {
                        System.err.println(exception);
                    }
                }
            }

            return new ArticleInfo(title,DOI,authorList,journal,volumeInfo,date,dateString);
        } else {
            System.err.println("ERROR: Status code is " + httpStatusCode);
            return null;
        }
    }
}
