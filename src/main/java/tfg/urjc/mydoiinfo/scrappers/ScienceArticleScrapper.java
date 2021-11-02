package tfg.urjc.mydoiinfo.scrappers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.stream.Collectors;

public class ScienceArticleScrapper extends JSOUPArticleScrapper {

    public ScienceArticleScrapper(String journalPrefix) {
        super(journalPrefix);
    }

    public void getArticleInfoFromDOI(String DOI){

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {
            Document document = getHtmlDocument(DOI);

            Element titulo = document.select("h1[property=\"name\"]").first();
            if(titulo != null)
                System.out.println(titulo.text());
            else
                System.out.println("Título Desconocido");

            Elements autores = document.select("span[property=\"author\"] > a:not([property])");
            if(autores == null || autores.size()==0){
                System.out.println("Autor: Desconocido");
            }else if(autores.size()==1){
                String autor = autores.first().text();
                System.out.println("Autor: " + autor);
            }else {
                String printAutores = autores.stream().map(elem->elem.text()).collect(Collectors.joining(", ","Autores: ",""));
                System.out.println(printAutores);
            }

            String revista = "Revista: ";
            Element elemRevista = document.select("span[property=\"name\"]").first();
            if(elemRevista != null){
                revista = revista + elemRevista.text();
                Element elemVolume = document.select("span.core-enumeration").first();
                if(elemVolume != null)
                    revista = revista + ", " + elemVolume.text();
            } else {
                revista = revista + "Desconocida";
            }
            System.out.println(revista);

            Element fecha = document.select("span[property=\"datePublished\"]").first();
            if(titulo != null)
                System.out.println("Fecha de publicación: " + fecha.text());
            else
                System.out.println("Fecha de publicación: Desconocida");
        } else
            System.out.println("ERROR: El código de estado es: " + httpStatusCode);
    }
}
