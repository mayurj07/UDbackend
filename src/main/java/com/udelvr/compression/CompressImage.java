package com.udelvr.compression;

import com.udelvr.exceptions.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CompressImage {

    public File compress(File imageFile) throws IOException {
        //File imageFile = new File("/Users/mayur/photu.jpg");


        //File compressedImageFile = new File("/Users/mayur/myimage_compressed.jpg");    //for local
        File compressedImageFile = new File("/home/ubuntu/UDbackend/images/compressedImages/compressed.jpg"); // for production
        InputStream is = new FileInputStream(imageFile);
        OutputStream os = new FileOutputStream(compressedImageFile);
        float quality = 0.5f;

        // create a BufferedImage as the result of decoding the supplied InputStream
        BufferedImage image = ImageIO.read(is);

        // get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext())
            throw new IllegalStateException("No writers found");

        ImageWriter writer = (ImageWriter) writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);
        ImageWriteParam param = writer.getDefaultWriteParam();

        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        // appends a complete image stream containing a single image and
        //associated stream and image metadata and thumbnails to the output
        writer.write(null, new IIOImage(image, null, null), param);

        // close all streams
        is.close();
        os.close();
        ios.close();
        writer.dispose();
        return compressedImageFile;
    }


    public File saveScaledImage(File file1){

        //String thumbnailPath  = "/Users/mayur/thumbnail.jpg";
        String thumbnailPath = "/home/ubuntu/UDbackend/images/thumbnails/thumbnail.jpg";
        try {

            BufferedImage sourceImage       = ImageIO.read(file1);
            //BufferedImage sourceImage     = ImageIO.read(new File(filePath));
            int width                       = sourceImage.getWidth();
            int height                      = sourceImage.getHeight();

            File thumnail = new File(thumbnailPath);
            if(width>height)
            {
                float extraSize         =    height-100;
                float percentHight      = (extraSize/height)*100;
                float percentWidth      = width - ((width/100)*percentHight);
                BufferedImage img       = new BufferedImage((int)percentWidth, 100, BufferedImage.TYPE_INT_RGB);
                Image scaledImage       = sourceImage.getScaledInstance((int)percentWidth, 100, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2      = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2                    = img.getSubimage((int)((percentWidth-100)/2), 0, 100, 100);

                ImageIO.write(img2, "jpg", thumnail);
            }
            else
            {
                float extraSize         =    width-100;
                float percentWidth      = (extraSize/width)*100;
                float  percentHight     = height - ((height/100)*percentWidth);
                BufferedImage img       = new BufferedImage(100, (int)percentHight, BufferedImage.TYPE_INT_RGB);
                Image scaledImage       = sourceImage.getScaledInstance(100,(int)percentHight, Image.SCALE_SMOOTH);
                img.createGraphics().drawImage(scaledImage, 0, 0, null);
                BufferedImage img2      = new BufferedImage(100, 100 ,BufferedImage.TYPE_INT_RGB);
                img2                    = img.getSubimage(0, (int)((percentHight-100)/2), 100, 100);

                ImageIO.write(img2, "jpg", thumnail);
            }

            return thumnail;

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("Error creating Thumbnail of Image.");
        }
    }


    public File convertToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public byte[] convertFileToByteArray(File file) throws IOException {
        FileInputStream fileInputStream=null;
        byte[] bFile = new byte[(int) file.length()];
        //convert file into array of bytes
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        }
        catch (Exception e)
        {
            throw new BadRequestException("Error Converting File to Byte Array.");
        }
        return bFile;
    }

}
