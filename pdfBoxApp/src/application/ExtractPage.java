package application;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ExtractPage {

    public int extract(String filePath, String pages) throws IOException {
        int ret = 0;
        int position = filePath.lastIndexOf("/");
        String path = filePath.substring(0, position);
        String fileName = filePath.substring(position+1);
        File file = new File(filePath) ;
        PDDocument document = PDDocument.load(file);
        PDDocument newdocument = new PDDocument();
        //get page numbers
        StringTokenizer st = new StringTokenizer(pages, ",");
        String pageArray[];
        int numPages = document.getNumberOfPages();
        int pageNos[] = new int[numPages];
        //initialize array
        for(int j=0; j<numPages; j++){
            pageNos[j] = -1;
        }
        int i=0;
        //get page numbers from string
        while(st.hasMoreTokens()){
            //check if it is a range
            String str = st.nextToken();
            if(str.contains("-")){
                pageArray = str.split("-");
                int start, end;
                start = Integer.parseInt(pageArray[0]);
                end  = Integer.parseInt(pageArray[1]);
                for(int j=start;j<=end;j++){
                    pageNos[i] = j-1;
                    i++;
                }
            }else{
                pageNos[i] = Integer.parseInt(str);
                pageNos[i] = pageNos[i]-1;//fix pagenumber indexing
                i++;
            }
        }
        for(int j=0; j<pageNos.length; j++){
            if(pageNos[j]<0)break;
            PDPage p = document.getPage(pageNos[j]);
            newdocument.addPage(p);
        }
        String timeStamp = new SimpleDateFormat("yy_MM_dd_HH_mm_ss").format(new Date());
        String destination = path + "/extracted_"+fileName+"_"+timeStamp+".pdf";
        newdocument.save(destination);
        document.close();
        newdocument.close();
        ret = 1;
        return ret;
    }

    public void extractPage(String page, String filePath){
        filePath = filePath.substring(5);
        int position = filePath.lastIndexOf("/");
        String path = filePath.substring(0, position);
        final String fp = filePath;
        String fileName = filePath.substring(position+1);
        int retVal = 0;
        try{
            retVal = extract(fp, page);
            if(retVal == 1){
                Alert a =new Alert("Done !", "Pages extracted.");
            }else{
                Alert a =new Alert("ERROR !", "Unable to extract pages.");
            }
        }catch(Exception e){
            e.printStackTrace();
            Alert a =new Alert("ERROR !", "Unable to extract pages.");
        }
    }
}
