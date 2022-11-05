package com.jackgharris.cosc2288.a2.core;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.*;
import java.util.Base64;


public class EasyImage implements Serializable {

    private final int width;
    private final int height;
    private final int[][] data;


    public EasyImage(Image image){
        //set our width and height!
        this.width = (int)image.getWidth();
        this.height = (int)image.getHeight();

        //create our data array
        this.data = new int[this.width][this.height];

        //load all the pixel data into the new data array
        PixelReader pixelReader = image.getPixelReader();
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.data[i][j] = pixelReader.getArgb(i, j);
            }
        }
    }

    public Image getImage() {
        WritableImage img = new WritableImage(width, height);

        PixelWriter w = img.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                w.setArgb(i, j, data[i][j]);
            }
        }

        return img;
    }

    public static String serialize(EasyImage easyImage){

        byte[] byteArray;

        try{

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(easyImage);
            byteArray = byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(byteArray);
    }

    public static EasyImage deSerialize(String byteString){

        byte[] bytes = Base64.getDecoder().decode(byteString);
        EasyImage easyImage;

        try{

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
            easyImage =  (EasyImage)objectInput.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return easyImage;
    }



}
