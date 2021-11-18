package tfg.urjc.mydoiinfo.scrappers.articleScrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import tfg.urjc.mydoiinfo.scrappers.ArticleInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class IEEEArticleScrapper extends PhantomArticleScrapper {

    public IEEEArticleScrapper(String[] journalPrefixList) {
        super(journalPrefixList);
    }

    @Override
    public ArticleInfo getArticleInfoFromDOI(String DOI) {

        int httpStatusCode = getHTTPStatusCode(DOI);

        if (httpStatusCode == 200) {

            PhantomJSDriver driver = getPhantomDriver();

            driver.get(DOI);

            WebElement titleElement;
            String title = null;
            try {
                titleElement = driver.findElementByClassName("document-title");
                if (titleElement != null)
                    title = titleElement.getText();
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

            try {
                //Perform a click to open the metadata information section of the page and obtain the necessary data
                driver.executeScript("document.querySelector(\".icon-caret-abstract\").click();");
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

            WebElement metadataJournal;
            String journal = null;
            String volumeInfo = null;
            try {
                metadataJournal = driver.findElement(By.cssSelector(".metadata-container > .stats-document-abstract-publishedIn"));
                if (metadataJournal != null && metadataJournal.findElement(By.cssSelector("a")) != null) {
                    journal = metadataJournal.findElement(By.cssSelector("a")).getText();
                    List<WebElement> metadataVolumeIssue = metadataJournal.findElements(By.cssSelector("span"));
                    if (metadataVolumeIssue != null && metadataVolumeIssue.size() >= 2) {
                        volumeInfo = metadataVolumeIssue.get(0).getText();
                        WebElement metadataIssue = metadataVolumeIssue.get(1).findElement(By.cssSelector("a"));
                        if (metadataIssue != null) {
                            volumeInfo = volumeInfo + ", " + metadataIssue.getText();
                        }
                        WebElement metadataPages = driver.findElement(By.cssSelector(".metadata-container > .row > .col-12 > div"));
                        if (metadataPages != null) {
                            volumeInfo = volumeInfo + ", pp " + metadataPages.getText().replace("Page(s)\n", "");
                        }
                    }
                }
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

            WebElement dateElement;
            Date date = null;
            String dateString = null;
            try {
                dateElement = driver.findElement(By.cssSelector(".metadata-container > .row > .col-12 > .doc-abstract-pubdate"));
                if (dateElement != null) {
                    dateString = dateElement.getText().replace("Date of Publication\n", "");
                    SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                    try {
                        date = format.parse(dateString);
                    } catch (ParseException exception) {
                        System.err.println(exception);
                    }
                }
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

            try {
                //Open the all authors page to obtain the authors info
                driver.executeScript("document.querySelector('.authors-viewall-link').click();");
                driver.executeScript("document.querySelector('a[routerlink=\"authors\"]').click();");
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

            List<WebElement> authors;
            List<String> authorList = null;
            try {
                authors = driver.findElements(By.cssSelector(".author-card > div > div > div > a"));
                if (authors != null && authors.size() > 0) {
                    if (authors.size() == 1) {
                        authorList = new ArrayList<>();
                        authorList.add(authors.get(0).getText());
                    } else {
                        authorList = authors.stream().map(elem -> elem.getText().replaceAll(";","")).filter(elem-> (elem!=null && elem!="" && elem!=" " && elem.length()>0)).collect(Collectors.toList());
                    }
                }
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }

            driver.quit();

            return new ArticleInfo(title, DOI, authorList, journal, volumeInfo, date, dateString);
        } else {
            System.err.println("ERROR: Status code is " + httpStatusCode + " scrapping DOI " + DOI);
            return null;
        }
    }
}
