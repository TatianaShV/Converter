package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

import ru.netology.graphics.image.TextColorSchema;

public class Converter implements TextGraphicsConverter {

    protected int maxWidth;
    protected int maxHeight;
    protected double maxRatio;
    protected int width;
    protected int height;
    protected double ratio;
    protected TextColorSchema textColorSchema;


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        width = img.getWidth();
        height = img.getHeight();
        int newWidth = 0;
        int newHeight = 0;
        if (maxRatio != 0.0) {
            ratio = width / height;

            if (maxRatio <= ratio) {
                throw new BadImageSizeException(maxRatio, ratio);

            }
            ratio = height / width;
            if (maxRatio <= ratio) {
                throw new BadImageSizeException(maxRatio, ratio);
            }
        }
        if (maxWidth != 0) {
            if (maxWidth < width) {
                newHeight = height / (width / maxWidth);
                newWidth = maxWidth;
            }
        }
        if (maxHeight != 0) {
            if (maxHeight < height) {
                newWidth = width / (height / maxHeight);
                newHeight = maxHeight;
            }
        } else if (maxWidth >= width && maxHeight >= height
                || maxHeight == 0 && maxWidth == 0) {
            newWidth = width;
            newHeight = height;
        }
        char[][] symbol = new char[newHeight][newWidth];
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {


                int color = bwRaster.getPixel(w, h, new int[3])[0];
                TextColorSchema schema = new Schema();


                if (textColorSchema != null) {
                    schema = getTextColorSchema();
                }

                char c = schema.convert(color);

                symbol[h][w] = c;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (char[] text : symbol) {
            sb.append(text);
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        if (maxRatio >= 0) {
            this.maxRatio = maxRatio;
        }

    }

    public double getMaxRatio() {
        return maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.textColorSchema = schema;
    }

    public TextColorSchema getTextColorSchema() {
        return textColorSchema;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
