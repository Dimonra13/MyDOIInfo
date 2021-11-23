package tfg.urjc.mydoiinfo.services.dataReaders;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.CategoryRanking;
import tfg.urjc.mydoiinfo.domain.entities.JCRRegistry;
import tfg.urjc.mydoiinfo.domain.entities.Journal;
import tfg.urjc.mydoiinfo.domain.repositories.CategoryRankingRepository;
import tfg.urjc.mydoiinfo.domain.repositories.JCRRegistryRepository;
import tfg.urjc.mydoiinfo.domain.repositories.JournalRepository;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

@Service
public class JCRDataReaderService {

    @Autowired
    JournalRepository journalRepository;

    @Autowired
    JCRRegistryRepository jcrRegistryRepository;

    @Autowired
    CategoryRankingRepository categoryRankingRepository;

    public void readJCRInfo(int year, String field) {

        File file = new File("./JCRData/" + field + "_" + year + ".txt");
        String data = null;
        try {
            data = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //Check if the file is empty
        if (data.equals("")){
            System.err.println("File"+"./JCRData/" + field + "_" + year + ".txt"+" is empty.");
            return;
        }

        String journalField = (field.equals("science")) ? "Sciences" : ((field.equals("social")) ? "Social Sciences" : null);

        //The short title of the first JCRRegistry in the current file, it will be used to check if the data has already been added for old JCRRegistries that don't have category info
        String firstShortTitle = data.split("</td>")[0].replace("<td class=\" sorting_1\">","").replace("<tr class=\"odd\">","");
        //Check if the data has already been added to the database to prevent duplicates
        if(jcrRegistryRepository.findFirstByYearAndCategoryRankingListJournalField(year,journalField)!=null ||
                //For old JCRData that don't have categoryInfo to check, it's necessary to verify if the first JCRRegistry has already been added
                ( year <= 2010 && jcrRegistryRepository.findFirstByYearAndJournalShortTitleIgnoreCase(year,firstShortTitle)!=null &&
                        //Check that the jcrRegistry really don't have category data
                        jcrRegistryRepository.findFirstByYearAndJournalShortTitleIgnoreCase(year,firstShortTitle).getCategoryRankingList().size()==0
                )
        ){
            System.out.println("File"+"./JCRData/" + field + "_" + year + ".txt"+" data has already been read.");
            return;
        }

        //If the file exists, is not empty and hasn't been already read the program proceeds to read the file
        System.out.println("Start reading the file "+file.getName()+".");
        Arrays.stream(data.split("</tr>"))
                .map(elem -> elem.replaceAll("</td>", "").replace("<tr class=\"odd\">", "").replace("<tr class=\"even\">", "").replaceAll("&amp;", "&"))
                .forEach(cleanedElement ->
                {
                    //Title-short,cat-short,cat,title,impact_factor,quartile,impact_factor_five_year,cat_ranking
                    String[] splitedJCR = cleanedElement.split("<td class=\"\">");
                    //If the length of splitedJCR is less than 8 it means there is an error and should not be stored in the database
                    if (splitedJCR.length>=8){
                        //Create the CategoryRanking object
                        CategoryRanking categoryRanking = null;
                        if (!splitedJCR[2].equals("") && !splitedJCR[2].equals("N/D") && !splitedJCR[2].equals(" "))
                            categoryRanking = new CategoryRanking(splitedJCR[2], splitedJCR[7],journalField);
                        //Check if the corresponding JCRRegistry already exists
                        JCRRegistry jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year, splitedJCR[3]);
                        if (jcrRegistry != null) {
                            //Continue only if the categoryRanking exists
                            if(categoryRanking != null){
                                //Check if a ranking has already been added for the current category in the existing JCRRegistry (Prevents duplicates in case of data errors)
                                CategoryRanking finalCategoryRanking = categoryRanking;
                                boolean alreadyAdded = jcrRegistry.getCategoryRankingList().stream()
                                        .filter(catRanking -> catRanking.getName().equals(finalCategoryRanking.getName()) && catRanking.getJournalField().equals(finalCategoryRanking.getJournalField()))
                                        .count() > 0;
                                if(!alreadyAdded){
                                    //Save the new categoryRanking
                                    categoryRanking = categoryRankingRepository.save(categoryRanking);
                                    //Add the created categoryRanking to the JCRRegistry
                                    jcrRegistry.getCategoryRankingList().add(categoryRanking);
                                    jcrRegistryRepository.save(jcrRegistry);
                                } else {
                                    System.err.println("Duplicated CategoryRanking: " + categoryRanking.toString());
                                }
                            } else {
                                System.err.println("Null Category, the category field value is : " + splitedJCR[2]);
                            }
                        } else { //Create the JCRRegistry if it doesn't exist
                            //Check if the corresponding journal already exists
                            Journal journal = journalRepository.findFirstByTitleIgnoreCase(splitedJCR[3]);
                            //Create the journal if it doesn't exist
                            if (journal == null) {
                                journal = new Journal(splitedJCR[3], splitedJCR[0].replace("<td class=\" sorting_1\">",""));
                                journal = journalRepository.save(journal);
                            }
                            Locale spanish = new Locale("es", "ES");
                            NumberFormat numberFormat = NumberFormat.getInstance(spanish);

                            Float impactFactor = null;
                            if(!splitedJCR[4].equals("N/D")){
                                try {
                                    impactFactor = numberFormat.parse(splitedJCR[4]).floatValue();
                                } catch (Exception exception) {
                                    System.err.println(exception.getMessage());
                                }
                            }
                            Float impactFactorFiveYear = null;
                            if(!splitedJCR[6].equals("N/D")){
                                try {
                                    impactFactorFiveYear = numberFormat.parse(splitedJCR[6]).floatValue();
                                } catch (Exception exception) {
                                    System.err.println(exception.getMessage());
                                }
                            }

                            jcrRegistry = new JCRRegistry(year, journal, impactFactor, impactFactorFiveYear, splitedJCR[5]);
                            //Add the new categoryRanking to the new JCRRegistry only if said categoryRanking is not null
                            if(categoryRanking != null){
                                //Save the new categoryRanking
                                categoryRanking = categoryRankingRepository.save(categoryRanking);
                                //Add the created categoryRanking to the JCRRegistry
                                jcrRegistry.getCategoryRankingList().add(categoryRanking);
                            } else {
                                System.err.println("Null Category, the category field value is : " + splitedJCR[2]);
                            }
                            jcrRegistry = jcrRegistryRepository.save(jcrRegistry);
                            //Add the created JCRRegistry to the journal
                            journal.getJcrRegistries().add(jcrRegistry);
                            journalRepository.save(journal);
                        }
                    } else {
                        System.err.println("Error reading line: "+cleanedElement);
                    }
                });
        System.out.println("Finish reading the file "+file.getName()+".");
    }
}
