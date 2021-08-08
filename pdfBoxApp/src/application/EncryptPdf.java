package application;

import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptPdf {
    private String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";

    public void encryptPdf(String filePath, String pwd){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pwd);
        filePath = filePath.substring(5);
        int position = filePath.lastIndexOf("/");
        String path = filePath.substring(0, position);
        final String fp = filePath;
        String fileName = filePath.substring(position+1);
        int retVal = 0;
        if(m.matches() == true){
            try{
                retVal = encrypt(fp, pwd);
                if(retVal == 1){
                    Alert a =new Alert("Done !", "Password set.");
                }else{
                    Alert a =new Alert("ERROR !", "Unable to encrypt.");
                }
            }catch(Exception e){
                Alert a =new Alert("ERROR !", "Unable to encrypt.");
            }
        }
        else{
            Alert a = new Alert("ERROR!", "Password should have atleast one number and a special character");
        }

//        encrypt.setText("File selected: "+ fileName);
//        userPwd.setVisible(true);
//        userPwd.setPromptText("Enter user password");
//        submitPwd.setVisible(true);
//        submitPwd.setOnAction(new EventHandler<ActionEvent>(){
//
//            @Override
//            public void handle(ActionEvent arg0) {
//                // after password is submitted hide input field
//                String pwd = userPwd.getText();
//                userPwd.setVisible(false);
//                submitPwd.setVisible(false);
//                int retVal = 0;
//                try{
//                    retVal = encrypt(fp, pwd);
//                    if(retVal == 1){
//                        encrypt.setText("");
//                        Alert a =new Alert("Done !", "Password set.");
//                    }else{
//                        encrypt.setText("");
//                        Alert a =new Alert("ERROR !", "Unable to encrypt.");
//                    }
//                }catch(Exception e){
//                    encrypt.setText("");
//                    Alert a =new Alert("ERROR !", "Unable to encrypt.");
//                }
//
//            }});

    }


    public int encrypt(String filePath, String pwd) throws IOException {
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
}
