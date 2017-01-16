package groovycia2;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.logging.Level;

public class CustomXMLHandler {

    private static String decodedPathCommunity;

    public static String getCommunityPath(){
        try{
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = URLDecoder.decode(path, "UTF-8");

            decodedPathCommunity = path.substring(0, path.lastIndexOf("/")) + "/community.xml";

            if (new File(decodedPathCommunity).exists()) {
                return decodedPathCommunity;
            }

        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
        return null;
    }

}
