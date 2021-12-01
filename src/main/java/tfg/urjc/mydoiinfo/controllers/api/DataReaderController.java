package tfg.urjc.mydoiinfo.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tfg.urjc.mydoiinfo.services.dataReaders.ConferenceDataReaderService;
import tfg.urjc.mydoiinfo.services.dataReaders.JCRDataReaderService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class DataReaderController {

    @Autowired
    JCRDataReaderService jcrDataReaderService;

    @Autowired
    ConferenceDataReaderService conferenceDataReaderService;

    @PostMapping("/JCR")
    public ResponseEntity<String> updatedJCRInfo(@RequestPart("file") MultipartFile dataFile,
                                                 @RequestParam("journal-field") String journalField,
                                                 @RequestParam("year") Integer year){
        if(dataFile==null || dataFile.getOriginalFilename()==null)
            return new ResponseEntity<>("It is necessary to send a file with the JCR information with the request", HttpStatus.BAD_REQUEST);
        if (journalField==null || journalField.equals("")
                || (!journalField.toUpperCase().contains("SOCIAL") && !journalField.toUpperCase().contains("SCIENCE")))
            return new ResponseEntity<>("It is necessary to specify the field of the journals whose JCR you wish to add (sciences or social sciences).", HttpStatus.BAD_REQUEST);
        String field = (journalField.toUpperCase().contains("SOCIAL") ? "social" : "science");
        String path = "./JCRData/"+ field + "_" + year + ".txt";
        ResponseEntity<String> response = saveFile(dataFile,path);
        if (response!=null && response.getStatusCode() != HttpStatus.OK)
            return response;
        else {
            Integer statusCode = jcrDataReaderService.readJCRInfo(year,field);
            if(statusCode==500){
                return new ResponseEntity<>("Error reading file "+ field + "_" + year + ".txt",HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (statusCode==400){
                return new ResponseEntity<>("File content already stored",HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("JCR data correctly stored in the database",HttpStatus.OK);
            }
        }
    }

    @PostMapping("/Conference")
    public ResponseEntity<String> updatedConferenceInfo(@RequestPart("file") MultipartFile dataFile){
        if(dataFile==null || dataFile.getOriginalFilename()==null)
            return new ResponseEntity<>("It is necessary to send a file with the conference information with the request", HttpStatus.BAD_REQUEST);
        if(!dataFile.getOriginalFilename().startsWith("GII-GRIN-SCIE-Conference-Rating-") || !dataFile.getOriginalFilename().endsWith("-Output.xlsx")){
            return new ResponseEntity<>("File name must follow the format GII-GRIN-SCIE-Conference-Rating-dd-MMM-yyyy-version-Output.xlsx",HttpStatus.BAD_REQUEST);

        }
        //Check the date on which data were updated
        Date updateDate = conferenceDataReaderService.getConferenceUpdateDate(dataFile.getOriginalFilename());
        if(updateDate==null){
            return new ResponseEntity<>("File name must follow the format GII-GRIN-SCIE-Conference-Rating-dd-MMM-yyyy-version-Output.xlsx (The date must follow the italian location format)",HttpStatus.BAD_REQUEST);
        }
        String path = "./ConferenceData/"+dataFile.getOriginalFilename();
        ResponseEntity<String> response = saveFile(dataFile,path);
        if (response!=null && response.getStatusCode() != HttpStatus.OK){
            return response;
        } else {
            Integer statusCode = conferenceDataReaderService.readConferneceInfo(dataFile.getOriginalFilename());
            if(statusCode==500){
                return new ResponseEntity<>("Error reading file "+dataFile.getOriginalFilename()+". The file could be corrupted or have an incorrect format",HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (statusCode==400){
                return new ResponseEntity<>("The file name may not have been formatted properly or the file may be empty",HttpStatus.BAD_REQUEST);
            } else if (statusCode==409){
                return new ResponseEntity<>("The data in the uploaded file is older than the data already stored in the database",HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>("Conference data correctly stored in the database",HttpStatus.OK);
            }
        }
    }

    private ResponseEntity<String> saveFile(MultipartFile dataFile, String path) {
        File file = new File(path);
        if (file.exists())
            return new ResponseEntity<>("File already exists", HttpStatus.BAD_REQUEST);
        byte[] fileContent;
        try {
            fileContent = dataFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Unable to read file contents", HttpStatus.BAD_REQUEST);
        }
        if (fileContent==null || fileContent.length==0)
            return new ResponseEntity<>("The file is empty", HttpStatus.BAD_REQUEST);
        Path convertedPath = Paths.get(path);
        try {
            Files.write(convertedPath,fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving file "+convertedPath.getFileName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
