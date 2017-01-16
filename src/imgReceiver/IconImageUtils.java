package imgReceiver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class IconImageUtils {

    public enum PixelFormat {RGBA8, RGB8, RGBA5551, RGB565, RGBA4, LA8, HILO8, L8, A8, LA4, L4, ETC1, ETC1A4}

    private static final int[] Convert5To8 = new int[]{0, 8, 16, 24, 32, 41, 49, 57, 65, 74, 82, 90, 98, 106, 115, 123, 131, 139, 148, 156, 164, 172, 180, 189, 197, 205, 213, 222, 230, 238, 246, 255};

    private static byte[] TempBytes = new byte[4];

    private static Color decodeColor(int val, PixelFormat pixelFormat){
        int num1 = 255;
        switch (pixelFormat){
            case RGBA8:
                int red1 = val >> 24 & 255;
                int green1 = val >> 16 & 255;
                int blue1 = val >> 8 & 255;
                return new Color(red1, green1, blue1, val & 255);
            case RGB8:
                int red2 = val >> 16 & 255;
                int green2 = val >> 8 & 255;
                int blue2 = val & 255;
                return new Color(red2, green2, blue2, num1);
            case RGBA5551:
                int red3 = Convert5To8[val >> 11 & 31];
                int green3 = Convert5To8[val >> 6 & 31];
                int blue3 = Convert5To8[val >> 1 & 31];
                return new Color(red3, green3, blue3, (val & 1) == 1 ? 255 : 0);
            case RGB565:
                int red4 = Convert5To8[val >> 11 & 31];
                int green4 = (val >> 5 & 63) * 4;
                int blue4 = Convert5To8[val & 31];
                return new Color(red4, green4, blue4);
            case RGBA4:
                return new Color(17 * (val >> 12 & 15), 17 * (val >> 8 & 15), 17 * (val >> 4 & 15), 17 * (val & 15));
            case LA8:
                int num2 = val >> 8;
                return new Color(num2, num2, num2, val & 255);
            case HILO8:
                int num3 = val >> 8;
                return new Color(num3, num3, num3, num1);
            case L8:
                return new Color(val, val, val, num1);
            case A8:
                return new Color(num1, num1, num1, val);
            case LA4:
                int num4 = val >> 4;
                return new Color(num4, num4, num4, val & 15);
            default:
                return new Color(255, 255, 255);
        }
    }

    private static void decodeTile(int iconSize, int tileSize, int ax, int ay, BufferedImage img, ByteArrayInputStream is, PixelFormat pixelFormat) throws Exception{
        if(tileSize == 0){
            is.read(TempBytes,0, 2);

            int i = ((int) TempBytes[1] << 8) + (int) (TempBytes[0]& 0xFF);

            img.setRGB(ax, ay, decodeColor(i, pixelFormat).getRGB());
        }else{
            int num1 = 0;
            while(num1 < iconSize){
                int num2 = 0;
                while(num2 < iconSize){
                    decodeTile(tileSize, tileSize/2, num2 + ax, num1 + ay, img, is, pixelFormat);
                    num2 += tileSize;
                }
                num1 += tileSize;
            }
        }
    }

    public static BufferedImage readImageFromStream(ByteArrayInputStream is, int width, int height, PixelFormat pixelFormat) throws Exception{
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
        int ay = 0;
        while(ay < height){
            int ax = 0;
            while(ax < width){
                decodeTile(8, 8, ax, ay, img, is, pixelFormat);
                ax += 8;
            }
            ay += 8;
        }
        return img;
    }

}
