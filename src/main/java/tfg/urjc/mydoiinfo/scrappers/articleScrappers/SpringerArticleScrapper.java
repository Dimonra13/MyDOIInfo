package tfg.urjc.mydoiinfo.scrappers.articleScrappers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SpringerArticleScrapper extends JSOUPArticleScrapper {

    public SpringerArticleScrapper(String[] journalPrefixList) {
        super(journalPrefixList);
    }

    public ArticleInfo getArticleInfoFromDOI(String DOI){

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(DOI);
            if(document==null){
                System.err.println("Error scrapping Springer DOI: "+DOI);
                return null;
            }

            Element titleElement = document.select("h1.c-article-title").first();
            String title = null;
            if(titleElement != null)
                title = titleElement.text();

            Elements authors = document.select("a[data-test=\"author-name\"]");
            List<String> authorList = null;
            if(authors != null && authors.size()>0){
                if(authors.size()==1){
                    authorList = new ArrayList<>();
                    authorList.add(authors.first().text());
                }else {
                    authorList = authors.stream().map(elem->elem.text()).collect(Collectors.toList());
                }
            }


            Element elemJournal = document.select("i[data-test=\"journal-title\"]").first();
            String journal = null;
            String volumeInfo = null;
            if(elemJournal != null){
                journal = elemJournal.text();
                Element elemVolume = document.select("b[data-test=\"journal-volume\"]").first();
                if(elemVolume != null)
                    volumeInfo = elemVolume.text().replaceAll("\u00A0"," ");
                Element elemMetadataInfo = document.select("p.c-article-info-details").first();
                if(elemMetadataInfo != null){
                    String[] splitedMetadataInfo = elemMetadataInfo.text().split("pages ");
                    if(splitedMetadataInfo.length>1){
                        String[] splitedPagesInfo = splitedMetadataInfo[1].split(" ");
                        if (splitedPagesInfo.length>0){
                            volumeInfo = volumeInfo + ", pp " + splitedPagesInfo[0];
                        }
                    }
                }
            }

            Element dateElement = document.select("time").first();
            Date date = null;
            String dateString = null;
            if(dateElement != null){
                dateString = dateElement.text();
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                try {
                    date = format.parse(dateString);
                } catch (ParseException ex) {
                    SimpleDateFormat otherFormat = new SimpleDateFormat("MMMM yyyy", Locale.US);
                    try {
                        date = otherFormat.parse(dateString);
                    } catch (ParseException exception) {
                        System.err.println(exception);
                    }
                }
            }

            return new ArticleInfo(title,DOI,authorList,journal,volumeInfo,date,dateString);
        } else {
            System.err.println("ERROR: Status code is " + httpStatusCode + " scrapping DOI " + DOI);
            return null;
        }
    }
}
