package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileControler {

//    private static int defaultCountOfImages = 0;

    // https://mkyong.com/swing/java-swing-jfilechooser-example/
//    public static void setDefaultCountOfImages(int count){
//        defaultCountOfImages = count;
//    }
//    public static int getDefaultCountOfImages(){
//        return defaultCountOfImages;
//    }
    public static ArrayList<String> getAllImages(){
        ArrayList<String> defaultTextures = new ArrayList<>();
        File texturesFolder = new File("res/textures");
        File [] listOfFiles = texturesFolder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                defaultTextures.add(listOfFiles[i].getName());
            }
        }
        return defaultTextures;
    }
    public static boolean fileExists(String name){
        Boolean result = false;
        File file = new File("res/textures/"+name);
        if(file.exists()){
            result = true;
        }
        return result;
    }
 public static String loadImage(){

     String path = "";
     JFileChooser jfc = new JFileChooser();
     FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Images Only","jpg");
     jfc.setFileFilter(filter);

     int returnValue = jfc.showOpenDialog(null);

     if (returnValue == JFileChooser.APPROVE_OPTION) {
         File selectedFile = jfc.getSelectedFile();
//         System.out.println(selectedFile.getAbsolutePath());
         String filePath = selectedFile.getAbsolutePath();

         try {
             Files.copy(selectedFile.toPath(), Paths.get("res/textures", selectedFile.getName()));
         } catch (IOException e) {
             e.printStackTrace();
         }
         path = selectedFile.getName();
     }
     return path;
 }
}
