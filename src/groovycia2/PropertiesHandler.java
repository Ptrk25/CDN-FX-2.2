package groovycia2;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.logging.Level;

public class PropertiesHandler {

    private static Properties p = new Properties();
    private static String decodedPath;

    public static void createFile(){
        try{
            if(getPath() == null){
                new File(decodedPath).createNewFile();
                DebugLogger.log("Properties file created!", Level.INFO);
            }
        }catch (IOException e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    public static String getTMInputPath(){
        return getProperties("tmticketdb");
    }

    public static String getTMOutputPath(){
        return getProperties("tmoutputfolder");
    }

    public static String getTDAInputPath(){
        return getProperties("tdfile");
    }

    public static String getTDOutputPath(){
        return getProperties("tdfolder");
    }

    public static String getTDTikPath(){
        return getProperties("tdtikfolder");
    }

    public static void setProperties(String data, String content){
        try{
            data = data.replaceAll(":", "!");
            p.setProperty(content, data);
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    public static String getProperties(String content){
        try{
            if(getPath() != null){
                InputStream input = new FileInputStream(getPath());
                p.load(input);
                String data = p.getProperty(content);
                if(data != null){
                    data = data.replaceAll("!", ":");
                }
                return data;
            }
        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
        return null;
    }

    public static void saveProperties(){
        try {
            p.store(new FileOutputStream(getPath()), "Config File");
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            e.printStackTrace();
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    private static String getPath(){

        try{
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("GroovyFX.jar","");
            decodedPath = URLDecoder.decode(path, "UTF-8");

            decodedPath = decodedPath.substring(0, decodedPath.lastIndexOf("/")) + "/settings.properties";

            if(new File(decodedPath).exists()){
                if(DetectOS.returnOS().equals("Windows"))
                    return decodedPath;
                else if(DetectOS.returnOS().equals("Unix"))
                    return decodedPath;
                else if(DetectOS.returnOS().equals("Mac"))
                    return decodedPath;
                else
                    return null;
            }
            return null;
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
            return null;
        }
    }

}
