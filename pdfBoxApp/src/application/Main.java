package application;


import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Accordion;
import javafx.scene.text.TextAlignment;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox vb = new VBox();
            Label title = new Label("PDF BOX");
            Font font = Font.font("Verdana", FontWeight.EXTRA_BOLD, 25);
            title.setFont(font);
            title.setPadding(new Insets(20,50,50,50));
            Image img = new Image("UIControls/pdf.png");
            ImageView view = new ImageView(img);
            view.setPreserveRatio(true);
            view.setFitHeight(50);
            title.setGraphic(view);
            title.setAlignment(Pos.CENTER);
            title.setPrefWidth(500);
            title.setTextFill(Color.rgb(4,30,66));
            vb.getChildren().add(title);
            vb.getChildren().add(new Separator());
            pdfController p = new pdfController();
            Accordion accordion = new Accordion();

            //split pdf
            SplitPdf sp = new SplitPdf();
            TitledPane pane1 = new TitledPane();
            pane1.setText("split Pdf");
            AnchorPane apane1 = new AnchorPane();
            Label l1 = new Label("Split a PDF in two");
            l1.setPrefWidth(458);
            l1.setPrefHeight(80);
            l1.setTextAlignment(TextAlignment.CENTER);
            l1.setContentDisplay(ContentDisplay.TOP);
            l1.setLayoutX(39);
            l1.setLayoutX(21);
            Button splitBut = new Button("Upload");
            splitBut.setLayoutX(508);
            splitBut.setLayoutY(17);
            EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>()  {
                public void handle(ActionEvent e)
                {
                    sp.splitPdf();
                }
            };
            splitBut.setOnAction(event1);
            apane1.getChildren().add(l1);
            apane1.getChildren().add(splitBut);
            apane1.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane1.setContent(apane1);

            //merge pdf
            MergePdf mp = new MergePdf();
            TitledPane pane2 = new TitledPane();
            pane2.setText("Merge Pdf");
            AnchorPane apane2 = new AnchorPane();
            Label l2 = new Label("Merge two PDFs");
            l2.setPrefWidth(458);
            l2.setPrefHeight(80);
            l2.setTextAlignment(TextAlignment.CENTER);
            l2.setContentDisplay(ContentDisplay.TOP);
            l2.setLayoutX(39);
            l2.setLayoutX(21);
            Button mergeBut = new Button("Upload");
            mergeBut.setLayoutX(508);
            mergeBut.setLayoutY(17);
            mergeBut.setOnAction((event2) -> {
                mp.mergePdf();
            });
            apane2.getChildren().add(l2);
            apane2.getChildren().add(mergeBut);
            apane2.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane2.setContent(apane2);

            //extract Text
            ExtractText et = new ExtractText();
            TitledPane pane3 = new TitledPane();
            pane3.setText("Extract Text");
            AnchorPane apane3 = new AnchorPane();
            Label l3 = new Label("Extract Text from PDF");
            l3.setPrefWidth(458);
            l3.setPrefHeight(80);
            l3.setTextAlignment(TextAlignment.CENTER);
            l3.setContentDisplay(ContentDisplay.TOP);
            l3.setLayoutX(39);
            l3.setLayoutX(21);
            Button extractBut = new Button("Upload");
            extractBut.setLayoutX(508);
            extractBut.setLayoutY(17);
            EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>()  {
                public void handle(ActionEvent e)
                {
                    et.toText();
                }
            };
            extractBut.setOnAction(event3);
            apane3.getChildren().add(l3);
            apane3.getChildren().add(extractBut);
            apane3.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane3.setContent(apane3);

            //convert to image
            PdfToImage ptoi = new PdfToImage();
            TitledPane pane4 = new TitledPane();
            pane4.setText("Convert to Image");
            AnchorPane apane4 = new AnchorPane();
            Label l4 = new Label("Cnvert PDF to image");
            l4.setPrefWidth(458);
            l4.setPrefHeight(80);
            l4.setTextAlignment(TextAlignment.CENTER);
            l4.setContentDisplay(ContentDisplay.TOP);
            l4.setLayoutX(39);
            l4.setLayoutX(21);
            Button imgBut = new Button("Upload");
            imgBut.setLayoutX(508);
            imgBut.setLayoutY(17);
            ProgressIndicator imgProgress = new ProgressIndicator();
            imgProgress.setLayoutX(332);
            imgProgress.setLayoutY(40);
            imgProgress.setProgress(0);
            imgProgress.setVisible(false);
            imgBut.setOnAction((event4) -> {
                ptoi.getImg(imgProgress);
            });
            apane4.getChildren().add(l4);
            apane4.getChildren().add(imgBut);
            apane4.getChildren().add(imgProgress);
            apane4.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane4.setContent(apane4);

            //image to pdf
            ImagetoPdf itop = new ImagetoPdf();
            TitledPane pane5 = new TitledPane();
            pane5.setText("Convert image to PDF");
            AnchorPane apane5 = new AnchorPane();
            Label l5 = new Label("Convert I,age to PDF");
            l5.setPrefWidth(458);
            l5.setPrefHeight(80);
            l5.setTextAlignment(TextAlignment.CENTER);
            l5.setContentDisplay(ContentDisplay.TOP);
            l5.setLayoutX(39);
            l5.setLayoutX(21);
            Button jtopBut = new Button("Upload");
            jtopBut.setLayoutX(508);
            jtopBut.setLayoutY(17);
            Label imgPdfL = new Label();
            imgPdfL.setLayoutX(231);
            imgPdfL.setLayoutY(59);
            jtopBut.setOnAction((event5) -> {
                itop.convertJtoP();
                imgPdfL.setText("Files Selected : " + itop.getFileName());
            });
            imgPdfL.setText("");
            apane5.getChildren().add(l5);
            apane5.getChildren().add(jtopBut);
            apane5.getChildren().add(imgPdfL);
            apane5.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane5.setContent(apane5);

            //extract pages
            ExtractPage ep = new ExtractPage();
            FileSelector fsObj = new FileSelector();
            TitledPane pane6 = new TitledPane();
            pane6.setText("Extract Pages from Pdf");
            AnchorPane apane6 = new AnchorPane();
            Label l6 = new Label("Extract Pages from PDF");
            l6.setPrefWidth(458);
            l6.setPrefHeight(80);
            l6.setTextAlignment(TextAlignment.CENTER);
            l6.setContentDisplay(ContentDisplay.TOP);
            l6.setLayoutX(39);
            l6.setLayoutX(21);
            Button extractpBut = new Button("Upload");
            extractpBut.setLayoutX(508);
            extractpBut.setLayoutY(17);
            TextField pages = new TextField();
            pages.setLayoutX(345);
            pages.setLayoutY(56);
            pages.setPromptText("Enter range or numbers");
            Button submitNos = new Button("Submit");
            submitNos.setLayoutX(516);
            submitNos.setLayoutY(56);
            extractpBut.setOnAction((event6_1) -> {
                String fpath = fsObj.selectFile();
                submitNos.setOnAction((event6_2) -> {
                    String page = pages.getText();
                    ep.extractPage(page,fpath);
                });
            });
            apane6.getChildren().add(l6);
            apane6.getChildren().add(extractpBut);
            apane6.getChildren().add(pages);
            apane6.getChildren().add(submitNos);
            apane6.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane6.setContent(apane6);

            //encrypt pdf
            EncryptPdf enp = new EncryptPdf();
            TitledPane pane7 = new TitledPane();
            pane7.setText("Encrypt PDF");
            AnchorPane apane7 = new AnchorPane();
            Label l7 = new Label("Encrypt the PDF by giving password");
            l7.setPrefWidth(458);
            l7.setPrefHeight(80);
            l7.setTextAlignment(TextAlignment.CENTER);
            l7.setContentDisplay(ContentDisplay.TOP);
            l7.setLayoutX(39);
            l7.setLayoutX(21);
            Button encryptBut = new Button("Upload");
            encryptBut.setLayoutX(508);
            encryptBut.setLayoutY(17);
            PasswordField  usrPwd = new PasswordField();
            usrPwd.setLayoutX(355);
            usrPwd.setLayoutY(57);
            usrPwd.setPromptText("Enter the password");
            Button submitPwd = new Button("Submit");
            submitPwd.setLayoutX(516);
            submitPwd.setLayoutY(56);
            encryptBut.setOnAction((event7_1) -> {
                String fpath = fsObj.selectFile();
                submitPwd.setOnAction((event7_2) -> {
                    String pwd = usrPwd.getText();
                    enp.encryptPdf(fpath,pwd);
                });
//                imgPdfL.setText("Files Selected : " + itop.getFileName());
            });
//            imgPdfL.setText("");
            apane7.getChildren().add(l7);
            apane7.getChildren().add(encryptBut);
            apane7.getChildren().add(usrPwd);
            apane7.getChildren().add(submitPwd);
            apane7.setBackground(new Background(new BackgroundFill(Color.rgb(149,219,229),
                    CornerRadii.EMPTY,Insets.EMPTY)));
            pane7.setContent(apane7);

            accordion.getPanes().add(pane1);
            accordion.getPanes().add(pane2);
            accordion.getPanes().add(pane3);
            accordion.getPanes().add(pane4);
            accordion.getPanes().add(pane5);
            accordion.getPanes().add(pane6);
            accordion.getPanes().add(pane7);


            vb.getChildren().add(accordion);
            vb.setPrefWidth(600);
            vb.setPrefHeight(400);
            vb.setBackground(new Background(new BackgroundFill(Color.rgb(175, 234,220), CornerRadii.EMPTY, Insets.EMPTY)));

            Scene scene = new Scene(vb,Color.BLUEVIOLET);

            primaryStage.setScene(scene);

            primaryStage.show();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nikhil.main;
//
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.scene.layout.VBox;
//import javafx.fxml.FXMLLoader;
//
//
//public class Main extends Application {
//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			VBox root = (VBox)FXMLLoader.load(getClass().getResource("pdfbox.fxml"));
//			Scene scene = new Scene(root,700,350);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//}
