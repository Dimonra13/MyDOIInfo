package tfg.urjc.mydoiinfo.services.dataReaders;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.urjc.mydoiinfo.domain.entities.Conference;
import tfg.urjc.mydoiinfo.domain.repositories.ConferenceRepository;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class ConferenceDataReaderService {

    @Autowired
    ConferenceRepository conferenceRepository;

    public void readConferneceInfo(String fileName){
        if(!fileName.startsWith("GII-GRIN-SCIE-Conference-Rating-")){
            System.err.println("Error: File name must follow the format GII-GRIN-SCIE-Conference-Rating-dd-MMM-yyyy-version-Output.xlsx");
            return;
        }
        //Date on which data were updated
        Date updateDate = getConferenceUpdateDate(fileName);
        if(updateDate==null){
            System.err.println("Error: File name must follow the format GII-GRIN-SCIE-Conference-Rating-dd-MMM-yyyy-version-Output.xlsx");
            return;
        }
        /*
        If the data stored is more modern than the data read or with the same update date, the process is cancelled
        because the data to be read is not more modern than the data already read.
         */
        if(conferenceRepository.findFirstByUpdatedDateAfter(updateDate) != null || conferenceRepository.findFirstByUpdatedDate(updateDate) != null){
            System.err.println("The file "+fileName+" data is not updated, canceling the reading");
            return;
        }
        System.out.println("Starting to read the file "+fileName);
        //Read the xlsx file using Apache POI and create a workbook with all the data
        File file = new File("./ConferenceData/"+fileName);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (workbook==null)
            return;
        //Get the first sheet of the xlsx (All the data is in the first sheet)
        Sheet sheet = workbook.getSheetAt(0);
        //If the sheet doesn't exit or if it is empty finish the process
        if(sheet==null || sheet.getLastRowNum()<2){
            System.err.println("The file "+fileName+" is empty");
            return;
        }
        //Read all the rows of the sheet starting from the first row with data
        for(int rowIndex=2;rowIndex<=sheet.getLastRowNum();rowIndex++){
            Row currentRow = sheet.getRow(rowIndex);
            //If the row format is incorrect skip the row
            if(currentRow.getLastCellNum()<42)
                continue;
            //Read the Conference Title
            String title = null;
            if(currentRow.getCell(1)!=null && currentRow.getCell(1).getCellType().name().equals("STRING"))
                title=currentRow.getCell(1).getStringCellValue();
            //If the title is null, empty or Work in Progress skip this row
            if (title==null || title.equals("") || title.equals("Work in Progress"))
                continue;
            //Read the Conference GGSClass
            Integer gssClass = null;
            if(currentRow.getCell(3)!=null && currentRow.getCell(3).getCellType().name().equals("NUMERIC")){
                Double aux = currentRow.getCell(3).getNumericCellValue();
                gssClass = aux.intValue();
            }
            //Read the Conference GGSRating
            String gssRating = null;
            if(currentRow.getCell(4)!=null && currentRow.getCell(4).getCellType().name().equals("STRING"))
                gssRating= (currentRow.getCell(4).getStringCellValue().equals("Work in Progress")) ? null
                        : currentRow.getCell(4).getStringCellValue();
            //Read the Core Best Class
            String coreClass = null;
            if(currentRow.getCell(40)!=null && currentRow.getCell(40).getCellType().name().equals("STRING"))
                coreClass=currentRow.getCell(40).getStringCellValue();
            Conference conference = conferenceRepository.findFirstByTitleIgnoreCase(title);
            //If the conference already exists update the data, else create a new conference
            if(conference!=null){
                conference.setGgsClass(gssClass);
                conference.setGgsRating(gssRating);
                conference.setCoreClass(coreClass);
                conference.setUpdatedDate(updateDate);
            } else {
                conference = new Conference(title,gssClass,gssRating,coreClass,updateDate);
            }
            //Save the new/updated conference
            conferenceRepository.save(conference);
        }
        System.out.println("Finishing reading the file "+fileName);
    }

    private Date getConferenceUpdateDate(String fileName) {
        if (fileName==null)
            return null;
        String[] splitedFileName = fileName.replace("GII-GRIN-SCIE-Conference-Rating-","").split("-");
        if(splitedFileName==null || splitedFileName.length<3)
            return null;
        String dateString = splitedFileName[0]+"-"+splitedFileName[1]+"-"+splitedFileName[2];
        Date date;
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ITALIAN);
        try {
            date = format.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
        return date;
    }
}
