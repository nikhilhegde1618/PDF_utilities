package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class SplitPdf {
    FileSelector fsObj = new FileSelector();
    private String filePath;

    public void splitPdf(){
        filePath = fsObj.selectFile();
        filePath = filePath.substring(5);
        int position = filePath.lastIndexOf("/");
        String path = filePath.substring(0, position);
        try{
            String fp = filePath;
            //get the file and load document
            File file = new File(filePath) ;
            PDDocument document = PDDocument.load(file);
            //call the splitter to split the pdf
            Splitter splitter = new Splitter();
            List<PDDocument> Pages = splitter.split(document); // list of all pages of the pdf
            //need page divisions from user
            int numPages = Pages.size();
            document.close();

            //Get mid point for splitting from the user by using input slider
            Stage stage = new Stage();
            stage.setTitle("Input range");
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("inputSlider.fxml"));
            VBox container = (VBox)root.getChildren().get(1);
            Slider slider = (Slider) container.lookup("Slider");
            slider.setMin(1);
            slider.setMax(numPages);
            slider.setValue(1);
            slider.setBlockIncrement(1);
            Label smin = (Label)root.lookup("#smin");
            Label smax = (Label)root.lookup("#smax");
            Label sval = (Label)container.lookup("#sval");
            Button splitNow = (Button)container.lookup("#splitNow");
            smin.setText("1");
            smax.setText(""+numPages);
            sval.setText(String.valueOf((int) slider.getValue()));

            slider.valueProperty().addListener(new ChangeListener<Object>(){
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    sval.setText(String.valueOf((int) slider.getValue()));
                }

            });
            splitNow.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent arg0) {
                    sval.setText(String.valueOf((int) slider.getValue()));
                    stage.close();
                    //calling function to split
                    int retVal = 0;
                    try{
                        retVal = splitPDF(fp, (int) slider.getValue(), numPages);
                        if(retVal==1){
                            Alert a = new Alert("DONE !","Splitting complete, check source folder for the pdf.");
                        }else{
                            Alert a = new Alert("ERROR !", "Unable to split pdf.");
                        }
                    }catch(Exception e){
                        System.out.println(e);
                        Alert a = new Alert("ERROR !", "Unable to split pdf.");
                    }
                }
            });
            stage.setScene(new Scene(root, 500, 200));
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public int splitPDF(String filePath, int mid, int numPages) throws IOException {
        PDDocument document1 = new PDDocument();
        PDDocument document2 = new PDDocument();
        File file = new File(filePath) ;
        PDDocument document = PDDocument.load(file);
        Splitter splitter = new Splitter();
        List<PDDocument> Pages = splitter.split(document); // list of all pages of the pdf
        Iterator<PDDocument> it = Pages.listIterator();
        int i=1;

        while(it.hasNext()) {
            PDDocument  pd = it.next();
            PDPage p = pd.getPage(0);
            if(i<=mid){
                document1.addPage(p);
            }else{
                document2.addPage(p);
            }
            i++;

            if(i>numPages)break;
        }
        document1.save(filePath+"_part1.pdf");
        document2.save(filePath+"_part2.pdf");
        document.close();
        return 1;
    }
}
