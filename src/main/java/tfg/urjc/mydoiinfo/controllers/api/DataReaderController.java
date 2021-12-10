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

    @PostMapping("/jcr")
    public ResponseEntity<String> updatedJCRInfo(@RequestPart("file") MultipartFile dataFile,
                                                 @RequestParam("journal-field") String journalField,
                                                 @RequestParam("year") Integer year){
        //If the file is null or has no name, error 400 is returned
        if(dataFile==null || dataFile.getOriginalFilename()==null)
            return new ResponseEntity<>("It is necessary to send a file with the JCR information with the request", HttpStatus.BAD_REQUEST);
        //JournalField must contain "social" or "science", otherwise error 400 is returned
        if (journalField==null || journalField.equals("")
                || (!journalField.toUpperCase().contains("SOCIAL") && !journalField.toUpperCase().contains("SCIENCE")))
            return new ResponseEntity<>("It is necessary to specify the field of the journals whose JCR you wish to add (sciences or social sciences).", HttpStatus.BAD_REQUEST);
        //Create the path where the file will be saved
        String field = (journalField.toUpperCase().contains("SOCIAL") ? "social" : "science");
        String path = "./JCRData/"+ field + "_" + year + ".txt";
        //Save the file
        ResponseEntity<String> response = saveFile(dataFile,path);
        //If an error occurs during the file saving process, this error is returned
        if (response!=null && response.getStatusCode() != HttpStatus.OK)
            return response;
        else {
            //The data in the saved file is stored in the database
            Integer statusCode = jcrDataReaderService.readJCRInfo(year,field);
            //If an error occurs during the data storage process, this error is returned. If not, 200 OK is returned
            if(statusCode==500){
                return new ResponseEntity<>("Error reading file "+ field + "_" + year + ".txt",HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (statusCode==400){
                return new ResponseEntity<>("File content already stored",HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("JCR data correctly stored in the database",HttpStatus.OK);
            }
        }
    }

    @PostMapping("/conference")
    public ResponseEntity<String> updatedConferenceInfo(@RequestPart("file") MultipartFile dataFile){
        //If the file is null or has no name, error 400 is returned
        if(dataFile==null || dataFile.getOriginalFilename()==null)
            return new ResponseEntity<>("It is necessary to send a file with the conference information with the request", HttpStatus.BAD_REQUEST);
        //The file title must start with "GII-GRIN-SCIE-Conference-Rating-" and end with "-Output.xlsx", otherwise error 400 is returned
        if(!dataFile.getOriginalFilename().startsWith("GII-GRIN-SCIE-Conference-Rating-") || !dataFile.getOriginalFilename().endsWith("-Output.xlsx")){
            return new ResponseEntity<>("File name must follow the format GII-GRIN-SCIE-Conference-Rating-dd-MMM-yyyy-version-Output.xlsx",HttpStatus.BAD_REQUEST);

        }
        //The date on which data was updated is checked and error 400 is returned if the date cannot be parsed correctly
        Date updateDate = conferenceDataReaderService.getConferenceUpdateDate(dataFile.getOriginalFilename());
        if(updateDate==null){
            return new ResponseEntity<>("File name must follow the format GII-GRIN-SCIE-Conference-Rating-dd-MMM-yyyy-version-Output.xlsx (The date must follow the italian location format)",HttpStatus.BAD_REQUEST);
        }
        //Create the path where the file will be saved
        String path = "./ConferenceData/"+dataFile.getOriginalFilename();
        //Save the file
        ResponseEntity<String> response = saveFile(dataFile,path);
        //If an error occurs during the file saving process, this error is returned
        if (response!=null && response.getStatusCode() != HttpStatus.OK){
            return response;
        } else {
            //The data in the saved file is stored in the database
            Integer statusCode = conferenceDataReaderService.readConferneceInfo(dataFile.getOriginalFilename());
            //If an error occurs during the data storage process, this error is returned. If not, 200 OK is returned
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
        //Check if the file already exists
        File file = new File(path);
        if (file.exists())
            return new ResponseEntity<>("File already exists", HttpStatus.BAD_REQUEST);
        //Read the file content as a byte array
        byte[] fileContent;
        try {
            fileContent = dataFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Unable to read file contents", HttpStatus.BAD_REQUEST);
        }
        //If the file is empty error 400 is returned
        if (fileContent==null || fileContent.length==0)
            return new ResponseEntity<>("The file is empty", HttpStatus.BAD_REQUEST);
        //Save the file in the specified path
        Path convertedPath = Paths.get(path);
        try {
            Files.write(convertedPath,fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving file "+convertedPath.getFileName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //The file has been saved successfully so 200 Ok is returned
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
