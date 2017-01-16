package imgReceiver;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class IconImgInfoReceiver {

    private byte[] iconData;
    private String TitleID;
    private String name;
    private ArrayList<Boolean> regions;

    private BufferedImage smallImage, largeImage;

    public IconImgInfoReceiver(byte[] iconData, String TitleID) throws Exception{
        this.iconData = iconData;
        this.TitleID = TitleID;
        this.regions = new ArrayList<>();
    }

    private void seek(ByteArrayInputStream is, long pos){
        is.reset();
        is.skip(pos);
    }

    public void processData() throws Exception{
        switch(TitleID.substring(0, 4)){
            case "0004":
                int num1 = iconData[48];
                for(int i = 0; i < 7; ++i)
                    regions.add(((int) (num1 >> i) & 1) == 1);
                ByteArrayInputStream baos = new ByteArrayInputStream(iconData);
                seek(baos, 8272);
                this.smallImage = IconImageUtils.readImageFromStream(baos, 24, 24, IconImageUtils.PixelFormat.RGB565);
                this.largeImage = IconImageUtils.readImageFromStream(baos, 48, 48, IconImageUtils.PixelFormat.RGB565);
                name = new String(Arrays.copyOfRange(iconData, 208 + 512, 976));
        }
    }

    public String getName() {
        return name;
    }

    public String getRegion(){
        String region = "";

        for(int i = 0; i < regions.size(); i++){
            if(regions.get(i)){
                switch (i){
                    case 0:
                        return "JPN";
                    case 1:
                        return "USA";
                    case 2:
                        return "EUR";
                    case 3:
                        return "EUR";
                    case 4:
                        return "CHN";
                    case 5:
                        return "KOR";
                    case 6:
                        return "TWN";
                    default:
                        return "ALL";
                }
            }
        }

        return region;
    }

    public BufferedImage getSmallImage() {
        return smallImage;
    }

    public BufferedImage getLargeImage() {
        return largeImage;
    }
}
