package application;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class ExtractText {
    FileSelector fsObj = new FileSelector();
    private String filePath;
    public void toText(){
        filePath = fsObj.selectFile();
        filePath = filePath.substring(5);
        int position = filePath.lastIndexOf("/");
        String path = filePath.substring(0, position);
        String fileName = filePath.substring(position+1);

        try {
            String fp = filePath;
            //destination file
            String filename = path+"/converted.txt";
            File file = new File(filePath) ;
            PDDocument document = PDDocument.load(file);

            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();

            //Retrieving text from PDF document
            String text = pdfStripper.getText(document);

            //write to file
            FileOutputStream fos = new FileOutputStream(filename);
            PrintStream ps = new PrintStream(fos);
            ps.println(text);

            Alert a =new Alert("DONE !", "Extracted the pdf to a text file titled converted.");
            document.close();
        }catch (Exception e) {
            e.printStackTrace();
            Alert a =new Alert("ERROR !", "Unable to extract text.");
        }

    }
}
