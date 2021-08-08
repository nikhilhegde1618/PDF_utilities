package application;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;

public class ImagetoPdf {
    private String fileName;
    private String filePath;
    private String path;
    FileSelector fsObj = new FileSelector();

    public String getFileName(){
        return this.fileName;
    }

    public void convertJtoP(){
        filePath = fsObj.selectImg();
        filePath = filePath.substring(5);
        int position = filePath.lastIndexOf("/");
        path = filePath.substring(0,position);
        fileName = filePath.substring(position+1);
        try {
            String fp = filePath;
            String filename = path+"/"+fileName+".pdf";
            PDDocument document = new PDDocument();
            InputStream in = new FileInputStream(filePath);
            BufferedImage bimg = ImageIO.read(in);
            float width = bimg.getWidth();
            float height = bimg.getHeight();
            PDPage page = new PDPage(new PDRectangle(width, height));
            document.addPage(page);
            PDImageXObject img = PDImageXObject.createFromFile(filePath, document);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(img, 0, 0);
            contentStream.close();
            in.close();
            document.save(filename);
            document.close();
            Alert a =new Alert("DONE !", "converted image to pdf");
            document.close();
        }catch (Exception e) {
            e.printStackTrace();
            Alert a =new Alert("ERROR !", "Unable to convert.");
        }
    }
}
