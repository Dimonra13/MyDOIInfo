package tfg.urjc.mydoiinfo.controllers;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import tfg.urjc.mydoiinfo.services.dataReaders.ConferenceDataReaderService;
import tfg.urjc.mydoiinfo.services.dataReaders.JCRDataReaderService;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@Controller
public class DataReaderController implements CommandLineRunner {

    @Autowired
    private Environment environment;

    @Autowired
    JCRDataReaderService jcrDataReaderService;

    @Autowired
    ConferenceDataReaderService conferenceDataReaderService;

    @Override
    public void run(String... args) {
        if(!Arrays.stream(environment.getActiveProfiles()).anyMatch(elem -> elem.equals("test"))){
            File dataFolder = new File("./JCRData");
            if(dataFolder.isDirectory()){
                for(File dataFile: dataFolder.listFiles()){
                    if(!dataFile.isDirectory()){
                        String[] splitedName = dataFile.getName().split("_");
                        if (splitedName.length<2)
                            continue;
                        Integer year;
                        try {
                            year = Integer.parseInt(splitedName[1].replace(".txt",""));
                        }catch (Exception e){
                            e.printStackTrace();
                            continue;
                        }
                        jcrDataReaderService.readJCRInfo(year, splitedName[0]);
                    }
                }
            }
            conferenceDataReaderService.readConferneceInfo("GII-GRIN-SCIE-Conference-Rating-24-ott-2021-9.17.09-Output.xlsx");
        }
    }

}
