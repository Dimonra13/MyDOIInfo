package tfg.urjc.mydoiinfo.scrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import java.util.List;
import java.util.stream.Collectors;

public class IEEEArticleScrapper extends PhantomArticleScrapper {

    public IEEEArticleScrapper(String journalPrefix) {
        super(journalPrefix);
    }

    @Override
    public void getArticleInfoFromDOI(String DOI) {
        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {

            PhantomJSDriver driver = getPhantomDriver();

            driver.get(DOI);

            WebElement titulo;
            try{
                titulo = driver.findElementByClassName("document-title");
                if(titulo != null)
                    System.out.println(titulo.getText());
                else
                    System.out.println("Título Desconocido");
            }catch (Exception exception){
                System.out.println("Título Desconocido");
            }

            List<WebElement> autores;
            try{
                autores = driver.findElements(By.className("authors-info"));
                if(autores == null || autores.size()==0){
                    System.out.println("Autor: Desconocido");
                }else if(autores.size()==1){
                    String autor = autores.get(0).getText();
                    System.out.println("Autor: " + autor);
                }else {
                    String printAutores = autores.stream().map(elem->elem.getText()).collect(Collectors.joining(" ","Autores: ",""));
                    System.out.println(printAutores);
                }
            }catch (Exception exception){
                System.out.println("Autor: Desconocido");
            }


            try{
                driver.executeScript("document.querySelector(\".icon-caret-abstract\").click();");
            }catch (Exception exception){
                System.err.println(exception.getMessage());
            }


            String revista = "Revista: ";
            WebElement metadataRevista;
            try{
                metadataRevista = driver.findElement(By.cssSelector(".metadata-container > .stats-document-abstract-publishedIn"));
                if(metadataRevista != null && metadataRevista.findElement(By.cssSelector("a")) != null){
                    revista = revista + metadataRevista.findElement(By.cssSelector("a")).getText();
                    List<WebElement> metadataVolumeIssue = metadataRevista.findElements(By.cssSelector("span"));
                    if(metadataVolumeIssue != null && metadataVolumeIssue.size() >= 2){
                        revista = revista + ", " + metadataVolumeIssue.get(0).getText();
                        WebElement metadataIssue = metadataVolumeIssue.get(1).findElement(By.cssSelector("a"));
                        if(metadataIssue != null){
                            revista = revista + ", " + metadataIssue.getText();
                        }
                        WebElement metadataPaginas = driver.findElement(By.cssSelector(".metadata-container > .row > .col-12 > div"));
                        if(metadataPaginas != null){
                            revista = revista + ", pp "+ metadataPaginas.getText().replace("Page(s)\n","");
                        }
                    }
                } else {
                    revista = revista + "Desconocida";
                }
            }catch (Exception exception){
                if(revista.equals("Revista: "))
                    revista = revista + "Desconocida";
            }
            System.out.println(revista);


            WebElement fecha;
            try {
                fecha = driver.findElement(By.cssSelector(".metadata-container > .row > .col-12 > .doc-abstract-pubdate"));
                if(fecha != null){
                    System.out.println("Fecha de publicación: " + fecha.getText().replace("Date of Publication\n",""));
                } else {
                    System.out.println("Fecha de publicación: Desconocida");
                }
            }catch (Exception exception){
                System.out.println("Fecha de publicación: Desconocida");
            }

            driver.quit();

        } else
            System.out.println("ERROR: El código de estado es: " + httpStatusCode);
    }
}
