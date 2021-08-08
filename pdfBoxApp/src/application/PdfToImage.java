package application;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressIndicator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

class ConvertToImg extends Task {
    String filepath, filename;
    @Override
    protected Object call() throws Exception {
        int count = 0;
        File file = new File(filepath) ;
        PDDocument document = PDDocument.load(file);
        //Instantiating the PDFRenderer class
        PDFRenderer renderer = new PDFRenderer(document);
        count = document.getNumberOfPages();
        for (int page = 0; page < document.getNumberOfPages(); ++page){
            //update progress of the progress indicator
            this.updateProgress(page, count);
            BufferedImage image = renderer.renderImage(page);
            //Writing the image to a file
            String fname = filename+"_"+(page+1)+".jpg";
            ImageIO.write(image, "JPEG", new File(fname));
        }
        document.close();
        return count;
    }
    //constructor to initialize filepath and filename of the pdf
    ConvertToImg(String fp, String fn){
        this.filename = fn;
        this.filepath = fp;
    }

}

public class PdfToImage {
    private String filePath;
    FileSelector fsObj = new FileSelector();
    public void getImg(ProgressIndicator imgProgress){
        filePath = fsObj.selectFile();
        filePath = filePath.substring(5);
        int position = filePath.lastIndexOf("/");
        String path = filePath.substring(0, position);
        //create object of converttoimg class to execute conversion in concurrency
        ConvertToImg con;
        imgProgress.setVisible(false);
        imgProgress.setProgress(0);
        String fileName = filePath.substring(position+1);

        try {
            //create a new directory to store images, if directory exists then modify name to avoid conflict
            String fp = path+"/pdfToImage";
            File dir = new File(fp);
            if(!dir.exists()){
                dir.mkdirs();
            }else{
                String timeStamp = new SimpleDateFormat("yy_MM_dd_HH_mm_ss").format(new Date());
                fp = fp+"_"+timeStamp;
                dir = new File(fp);
                dir.mkdirs();
            }
            String filename = fp+"/image";
            con = new ConvertToImg(filePath, filename);
            //set properties for progress indicator
            imgProgress.setVisible(true);
            imgProgress.progressProperty().unbind();
            //bind progress indicator with the concurrent task
            imgProgress.progressProperty().bind(con.progressProperty());

            //Rendering an image from the PDF document
            con.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                    new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            Alert a =new Alert("DONE !", "Converted the pdf to images.");
                            //hide indicator after conversion is complete
                            imgProgress.setVisible(false);
                        }
                    });
            //start conversion in a separate thread
            new Thread(con).start();
        }catch (Exception e) {
            e.printStackTrace();
            Alert a =new Alert("ERROR !", "Unable to convert.");
        }
    }
}
