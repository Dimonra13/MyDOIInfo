package tfg.urjc.mydoiinfo.controllers;


import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
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

@Controller
public class JCRDataReader implements CommandLineRunner {

    @Autowired
    JournalRepository journalRepository;

    @Autowired
    JCRRegistryRepository jcrRegistryRepository;

    @Autowired
    CategoryRankingRepository categoryRankingRepository;

    @Override
    public void run(String... args) throws Exception {
        readJCRInfo(2020, "science");
    }

    public void readJCRInfo(int year, String field) throws IOException {

        File file = new File("./JCRData/" + field + "_" + year + ".txt");
        String journalField = (field.equals("science")) ? "Sciences" : ((field.equals("social")) ? "Social Sciences" : null);
        String data = FileUtils.readFileToString(file, "UTF-8");
        Arrays.stream(data.split("</tr>"))
                .map(elem -> elem.replaceAll("</td>", "").replace("sorting_1", "").replace("<tr class=\"odd\">", "").replace("<tr class=\"even\">", "").replaceAll("&amp;", "&"))
                .forEach(cleanedElement ->
                {
                    //Title-short,cat-short,cat,title,impact_factor,quartile,impact_factor_five_year,cat_ranking
                    String[] splitedJCR = cleanedElement.split("<td class=\"\">");
                    //Create the CategoryRanking object
                    CategoryRanking categoryRanking = new CategoryRanking(splitedJCR[2], splitedJCR[7],journalField);
                    //Check if the corresponding JCRRegistry already exists
                    JCRRegistry jcrRegistry = jcrRegistryRepository.findFirstByYearAndJournalTitleIgnoreCase(year, splitedJCR[3]);
                    if (jcrRegistry != null) {
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
                    } else { //Create the JCRRegistry if it doesn't exist
                        //Save the new categoryRanking
                        categoryRanking = categoryRankingRepository.save(categoryRanking);
                        //Check if the corresponding journal already exists
                        Journal journal = journalRepository.findFirstByTitleIgnoreCase(splitedJCR[3]);
                        //Create the journal if it doesn't exist
                        if (journal == null) {
                            journal = new Journal(splitedJCR[3], splitedJCR[0]);
                            journal = journalRepository.save(journal);
                        }
                        Locale spanish = new Locale("es", "ES");
                        NumberFormat numberFormat = NumberFormat.getInstance(spanish);

                        Float impactFactor = null;
                        try {
                            impactFactor = numberFormat.parse(splitedJCR[4]).floatValue();
                        } catch (Exception exception) {
                            System.err.println(exception.getMessage());
                        }
                        Float impactFactorFiveYear = null;
                        try {
                            impactFactorFiveYear = numberFormat.parse(splitedJCR[6]).floatValue();
                        } catch (Exception exception) {
                            System.err.println(exception.getMessage());
                        }

                        jcrRegistry = new JCRRegistry(year, journal, impactFactor, impactFactorFiveYear, splitedJCR[5]);
                        //Add the created categoryRanking to the JCRRegistry
                        jcrRegistry.getCategoryRankingList().add(categoryRanking);
                        jcrRegistry = jcrRegistryRepository.save(jcrRegistry);
                        //Add the created JCRRegistry to the journal
                        journal.getJcrRegistries().add(jcrRegistry);
                        journalRepository.save(journal);
                    }
                });
    }
}
