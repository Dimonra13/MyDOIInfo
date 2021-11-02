package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.stream.Collectors;

public class ACMArticleScrapper extends JSOUPArticleScrapper {

    public ACMArticleScrapper(String journalPrefix) {
        super(journalPrefix);
    }

    public void getArticleInfoFromDOI(String DOI){

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(DOI);

            Element titulo = document.select("h1.citation__title").first();
            if(titulo != null)
                System.out.println(titulo.text());
            else
                System.out.println("Título Desconocido");

            Elements autores = document.select("a.author-name");
            if(autores == null || autores.size()==0){
                System.out.println("Autor: Desconocido");
            }else if(autores.size()==1){
                String autor = autores.first().attr("title");
                System.out.println("Autor: " + autor);
            }else {
                String printAutores = autores.stream().map(elem->elem.attr("title")).collect(Collectors.joining(", ","Autores: ",""));
                System.out.println(printAutores);
            }

            String revista = "Revista: ";
            Element elemRevista = document.select("span.epub-section__title").first();
            if(elemRevista != null){
                revista = revista + elemRevista.text();
                Element elemVolume = document.select("span.comma-separator").first();
                if(elemVolume != null)
                    revista = revista + ", " + elemVolume.text();
                Element elemPaginas = document.select("span.epub-section__pagerange").first();
                if(elemPaginas != null)
                    revista = revista + ", " + elemPaginas.text();
            } else {
                revista = revista + "Desconocida";
            }
            System.out.println(revista);

            Element fecha = document.select("span.CitationCoverDate").first();
            if(titulo != null)
                System.out.println("Fecha de publicación: " + fecha.text());
            else
                System.out.println("Fecha de publicación: Desconocida");
        } else
            System.out.println("ERROR: El código de estado es: " + httpStatusCode);
    }
}
