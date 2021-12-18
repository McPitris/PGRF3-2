package main;


import lwjglutils.OGLTexture2D;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class FileControler {

    private static ArrayList<String> allTextureNames = new ArrayList<>();

    public static ArrayList<OGLTexture2D> getAllImages() {
        ArrayList<OGLTexture2D> defaultTextures = new ArrayList<>();
        File texturesFolder = new File("res/textures");
        File[] listOfFiles = texturesFolder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                try {
                    allTextureNames.add(listOfFiles[i].getName());
                    defaultTextures.add(new OGLTexture2D("textures/" + listOfFiles[i].getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(defaultTextures);
        return defaultTextures;
    }

    public static OGLTexture2D loadImage() {

        OGLTexture2D nTexture = null;
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileNameExtensionFilter("JPG Images Only", "jpg"));
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setCurrentDirectory(new File("res/textures/"));

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
//         System.out.println(selectedFile.getAbsolutePath());
            String filePath = selectedFile.getAbsolutePath();

            try {
                // bohužel jsem na to nepřišel, tak alespoň toto řešení
                // pokud je obrázek v textures, zobrazí se, pokud je uploadován z jiné složky, bude zkopírován a zobrazen po restartu
                if (!allTextureNames.contains(selectedFile.getName())) {
//                    BufferedImage picture = ImageIO.read(selectedFile);
                    Files.copy(selectedFile.toPath(), Paths.get("res/textures", selectedFile.getName()));
                    JOptionPane.showMessageDialog(null, "Obrázek bude zobrazen po až restartování aplikace! \n Je nutné vypnout a zapnout aplikaci.", "Upozornění", JOptionPane.WARNING_MESSAGE);
                } else {
                    nTexture = new OGLTexture2D("textures/" + selectedFile.getName());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return nTexture;
    }
}
