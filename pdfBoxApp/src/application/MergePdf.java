package application;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MergePdf {
    FileSelector fsObj = new FileSelector();
    private String filePath1, filePath2;
    public void mergePdf(){
        filePath1 = fsObj.selectFile();
        filePath1 = filePath1.substring(5);
        int position = filePath1.lastIndexOf("/");
        String path1 = filePath1.substring(0, position);
        String fileName1 = filePath1.substring(position+1);
        String filePath2 = fsObj.selectFile();
        filePath2 = filePath2.substring(5);
        position = filePath2.lastIndexOf("/");
        String path2 = filePath2.substring(0, position);

        String fileName2 = filePath2.substring(position+1);
        try{
            File file1 = new File(filePath1) ;
            PDDocument document1 = PDDocument.load(file1);
            File file2 = new File(filePath2) ;
            PDDocument document2 = PDDocument.load(file2);
            //using timestamp in filename to avoid conflict
            String timeStamp = new SimpleDateFormat("yy_MM_dd_HH_mm_ss").format(new Date());
            String destination = path1+ "/merged_"+ timeStamp+".pdf";
            //Instantiating PDFMergerUtility class
            PDFMergerUtility PDFmerger = new PDFMergerUtility();
            //Setting the destination file
            PDFmerger.setDestinationFileName(destination);

            //adding the source files
            PDFmerger.addSource(file1);
            PDFmerger.addSource(file2);

            //Merging the two documents
            PDFmerger.mergeDocuments();
            Alert a = new Alert("DONE !","Merging complete, check source folder for the pdf titled merged.");
            document1.close();
            document2.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
