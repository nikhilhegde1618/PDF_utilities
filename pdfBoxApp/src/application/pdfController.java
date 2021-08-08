package application;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.pdfbox.multipdf.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.io.*;
import java.util.logging.*;

import javax.imageio.ImageIO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/*Class that handles alert box
 * The constructor takes two arguments - header for alert box and alert message
 * Creates the alert box and sets labels
 */
class Alert{
	Alert(String heading, String msg) {
		Stage stageAlert = new Stage();
        stageAlert.setTitle("Alert");
    	VBox alertroot;
		try {
			alertroot = (VBox)FXMLLoader.load(getClass().getResource("alert.fxml"));
			Label done = (Label)alertroot.lookup("#done");
	    	Label alertMsg = (Label)alertroot.lookup("#successMsg");
	    	done.setText(heading);
			alertMsg.setText(msg);
			alertMsg.setWrapText(true);
			stageAlert.setScene(new Scene(alertroot, 400, 220));
	        stageAlert.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
/* Class to handle image conversion concurrently
 * The method for conversion executes progress indicator
 * while the class renders each page of pdf to images
 */
//class ConvertToImg extends Task{
//	String filepath, filename;
//	@Override
//	protected Object call() throws Exception {
//		int count = 0;
//		File file = new File(filepath) ;
//		PDDocument document = PDDocument.load(file);
//		//Instantiating the PDFRenderer class
//	    PDFRenderer renderer = new PDFRenderer(document);
//	    count = document.getNumberOfPages();
//		for (int page = 0; page < document.getNumberOfPages(); ++page){
//			//update progress of the progress indicator
//			this.updateProgress(page, count);
//	    	BufferedImage image = renderer.renderImage(page);
//		    //Writing the image to a file
//	    	String fname = filename+"_"+(page+1)+".jpg";
//		    ImageIO.write(image, "JPEG", new File(fname));
//	    }
//		document.close();
//		return count;
//	}
//	//constructor to initialize filepath and filename of the pdf
//	ConvertToImg(String fp, String fn){
//		this.filename = fn;
//		this.filepath = fp;
//	}
//
//}
public class pdfController implements Initializable{
	//Controls
	@FXML
	Button butSplit, butMerge, butText, butImg, butEncrypt, butRemove, butAppend, submitPwd, submitNo, submitNos, jtop ;
	@FXML
	Label l1, extractL1, extractL2, mergeL, encrypt, removeL, extract, imgPdfL;
	@FXML
	ProgressIndicator imgProgress;
	@FXML
	PasswordField userPwd;
	@FXML
	TextField pageNo, pages;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	//Method to call file chooser to get pdfs
	public String selectFile(){
		String s;
		FileChooser fc=new FileChooser();
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        fc.getExtensionFilters().add(extFilter);
		File selectedFile=fc.showOpenDialog(null);
		if(selectedFile !=null){
			s=selectedFile.getAbsolutePath();
			//modify path to change \ to /
			s="file:"+s.replaceAll("\\\\", "/");
			System.out.println(s);
		}
		else {
			s = "Error";
			System.out.println("invalid file");
		}
		return s;
	}
	//Method to call file chooser to get images
	public String selectImg(){
		String s;
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
	
	/* All methods first get the file from filechooser, set value of filepath
	 * modify path to get just the path - without filename and store name separately
	 * set label with selected filename
	 */
	//handler for split pdf called when upload button is clicked
	public void splitPdf(){
		String filePath = selectFile();
		filePath = filePath.substring(5);
		int position = filePath.lastIndexOf("/");
		String path = filePath.substring(0, position);
		
		String fileName = filePath.substring(position+1);
		l1.setText("File selected: "+ fileName);
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
			//Get mid point for splitting from the user
			Stage stage = new Stage();
	        stage.setTitle("Input range");
	        HBox root = (HBox)FXMLLoader.load(getClass().getResource("inputSlider.fxml"));
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
					l1.setText("File selected: "+ fileName + " to split at Page "+ (int)slider.getValue());
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
							l1.setText("");
							Alert a = new Alert("DONE !","Splitting complete, check source folder for the pdf.");
						}else{
							l1.setText("");
							Alert a = new Alert("ERROR !", "Unable to split pdf.");
						}
					}catch(Exception e){
						System.out.println(e);
						l1.setText("");
						Alert a = new Alert("ERROR !", "Unable to split pdf.");
					}
				}
	        	
	        });
	        /*slider.valueChangingProperty().addListener(new ChangeListener<Object>(){

				@Override
				public void changed(ObservableValue arg0, Object arg1, Object arg2) {
					if(!slider.isValueChanging()){
						sval.setText(String.valueOf((int) slider.getValue()));
						stage.close();
						//calling function to split
						split(iterator, (int) slider.getValue(), numPages );
					}
					
				}
	        	
	        });*/
	        stage.setScene(new Scene(root, 500, 200));
	        stage.show();
		}catch(Exception e){
			e.printStackTrace();
			l1.setText("Failed to load the selected file");
		}
		
	}
	/* Method that performs actual splitting action
	 * created two documents, iterate through the documents created after splitting
	 * add pages <= mid to first document and remaining to the second one
	 */
	@SuppressWarnings("resource")
	public int splitPDF(String filePath, int mid, int numPages) throws IOException{
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
	//TODO : expand for multiple files
	//handler for merge pdf called when upload button is clicked
	public void mergePdf(){
		String filePath1 = selectFile();
		filePath1 = filePath1.substring(5);
		int position = filePath1.lastIndexOf("/");
		String path1 = filePath1.substring(0, position);
		String fileName1 = filePath1.substring(position+1);
		mergeL.setText("Files selected: "+ fileName1);
		String filePath2 = selectFile();
		filePath2 = filePath2.substring(5);
		position = filePath2.lastIndexOf("/");
		String path2 = filePath2.substring(0, position);
		
		String fileName2 = filePath2.substring(position+1);
		mergeL.setText("Files selected: "+ fileName1 + " and "+ fileName2);
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
		      mergeL.setText("");
		      document1.close();
		      document2.close();
		}catch(Exception e){
			e.printStackTrace();
			mergeL.setText("Failed to load the selected files");
		}
		
	}
	//handler for getting text from pdf, called when upload button is clicked
	public void toText(){
		String filePath = selectFile();
		filePath = filePath.substring(5);
		int position = filePath.lastIndexOf("/");
		String path = filePath.substring(0, position);
		
		String fileName = filePath.substring(position+1);
//		extractL1.setText("File selected: "+ fileName);
		
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
//		    extractL1.setText("");
		    Alert a =new Alert("DONE !", "Extracted the pdf to a text file titled converted.");
		    document.close();
		}catch (Exception e) {
			e.printStackTrace();
//			extractL1.setText("");
			Alert a =new Alert("ERROR !", "Unable to extract text.");
		}
		
	}
	//handler for converting pdf to images, called when upload button is clicked
	@SuppressWarnings("unchecked")
	public void getImg(){
		String filePath = selectFile();
		filePath = filePath.substring(5);
		int position = filePath.lastIndexOf("/");
		String path = filePath.substring(0, position);
		//create object of converttoimg class to execute conversion in concurrency
		ConvertToImg con;
		imgProgress.setVisible(false);
		imgProgress.setProgress(0);
		String fileName = filePath.substring(position+1);
		extractL2.setText("File selected: "+ fileName);
		
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
                	extractL2.setText("");
                }
            });
		    //start conversion in a separate thread
            new Thread(con).start();
		}catch (Exception e) {
			e.printStackTrace();
			extractL2.setText("");
			Alert a =new Alert("ERROR !", "Unable to convert.");
		}
	}
	//handler for encrypting pdf called when upload button is clicked
	/*Encryption needs two keys
	 *owner password - set here
	 *user password - entered by user, this is the password user has to enter when pdf is opened 
	 */
	public void encryptPdf(){
		String filePath = selectFile();
		filePath = filePath.substring(5);
		int position = filePath.lastIndexOf("/");
		String path = filePath.substring(0, position);
		final String fp = filePath;
		String fileName = filePath.substring(position+1);
		encrypt.setText("File selected: "+ fileName);
		userPwd.setVisible(true);
		userPwd.setPromptText("Enter user password");
		submitPwd.setVisible(true);
		submitPwd.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent arg0) {
				// after password is submitted hide input field
				String pwd = userPwd.getText();
				userPwd.setVisible(false);
				submitPwd.setVisible(false);
				int retVal = 0;
				try{ 
					retVal = encrypt(fp, pwd);
					if(retVal == 1){
						encrypt.setText("");
						Alert a =new Alert("Done !", "Password set.");
					}else{
						encrypt.setText("");
						Alert a =new Alert("ERROR !", "Unable to encrypt.");
					}
				}catch(Exception e){
					encrypt.setText("");
					Alert a =new Alert("ERROR !", "Unable to encrypt.");
				}
				
			}});
		
	}
	//method to encrypt the file
	public int encrypt(String filePath, String pwd) throws  IOException{
		int ret = 0;
		File file = new File(filePath) ;
		PDDocument document = PDDocument.load(file);
			 
		//Creating access permission object
	      AccessPermission ap = new AccessPermission();         

	      //Creating StandardProtectionPolicy object - owner password, user password
	      StandardProtectionPolicy spp = new StandardProtectionPolicy("1234", pwd, ap);

	      //Setting the length of the encryption key
	      spp.setEncryptionKeyLength(128);

	      //Setting the access permissions
	      spp.setPermissions(ap);

	      //Protecting the document
	      document.protect(spp);

	      System.out.println("Document encrypted");

	      //Saving the document
	      document.save(filePath);
			
			document.close();
		ret = 1;
		return ret;
	}
	//handler for removing page from pdf, called when upload button is clicked
	public void removePage(){
		String filePath = selectFile();
		filePath = filePath.substring(5);
		int position = filePath.lastIndexOf("/");
		String path = filePath.substring(0, position);
		final String fp = filePath;
		String fileName = filePath.substring(position+1);
		removeL.setText("File selected: "+ fileName);
		pageNo.setVisible(true);
		
		submitNo.setVisible(true);
		submitNo.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String page = pageNo.getText();
				pageNo.setVisible(false);
				submitNo.setVisible(false);
				int retVal = 0;
				try{ 
					retVal = remove(fp, page);
					if(retVal == 1){
						removeL.setText("");
						Alert a =new Alert("Done !", "Page removed.");
					}else{
						removeL.setText("");
						Alert a =new Alert("ERROR !", "Unable to remove page.");
					}
				}catch(Exception e){
					removeL.setText("");
					Alert a =new Alert("ERROR !", "Unable to remove page.");
				}
				
			}});
	}
	//method for removing the page and saving the document
	public int remove(String filePath, String page) throws  IOException{
		int ret = 0;
		File file = new File(filePath) ;
		PDDocument document = PDDocument.load(file);
		//Removing the pages
		int p = Integer.parseInt(page);
	      document.removePage(p-1); //it starts at 0
	      System.out.println("page removed");

	      //Saving the document
	      document.save(filePath);

	      //Closing the document
	      document.close();
		ret = 1;
		return ret;
	}
	/* This method first gets all page numbers from the string user entered
	 * page numbers can be comma separated or specified as a range
	 * page numbers are modified by -1 as indexing starts at 0
	 * we add these pages into a new document and save it
	 */

	//NOT DOING
	//handler for extracting pages from pdf, called when upload button is clicked

	//handler for converting images to pdf, called when upload button is clicked
	/*Get the image, create a new Page with a rectangle sized equal to the image
	 * Create an image object from the file and write to the document
	 */

}
