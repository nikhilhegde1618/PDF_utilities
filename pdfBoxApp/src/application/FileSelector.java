package application;

import javafx.stage.FileChooser;
import java.io.File;

public class FileSelector {
    private String s;

    public String selectFile() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            s = selectedFile.getAbsolutePath();
            //modify path to change \ to /
            s = "file:" + s.replaceAll("\\\\", "/");
            System.out.println(s);
        } else {
            s = "Error";
            System.out.println("invalid file");
        }
        return s;
    }

    public String selectImg(){
        FileChooser fc=new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile=fc.showOpenDialog(null);
        if(selectedFile !=null){
            s=selectedFile.getAbsolutePath();
            s="file:"+s.replaceAll("\\\\", "/");
            System.out.println(s);
        }
        else {
            s = "Error";
            System.out.println("invalid file");
        }
        return s;
    }
}
