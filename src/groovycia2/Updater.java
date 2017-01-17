package groovycia2;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.logging.Level;

public class Updater {

    private final String version = "dev-2.2";
    private String updateURL = "http://ptrk25.github.io/GroovyFX/program/";

    public Updater(){

    }

    public boolean checkForUpdates(){
        DebugLogger.log("Checking for Update...", Level.INFO);
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(updateURL + "version.txt").openStream()));
            String inputLine, content="";

            while((inputLine = in.readLine()) != null){
                content = inputLine;
            }

            if(version.equals(content)){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            DebugLogger.log("Error while searching for updates!", Level.WARNING);
        }
        return false;
    }

    public boolean update(){
        try{
            DebugLogger.log("Updating...", Level.INFO);
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            ReadableByteChannel in = Channels.newChannel(new URL(updateURL + "gfx.jar").openStream());
            FileChannel out = new FileOutputStream(path).getChannel();

            out.transferFrom(in, 0, Long.MAX_VALUE);
            in.close();
            out.close();

            DebugLogger.log("Update successful!", Level.INFO);
            return true;

        }catch(Exception e){
            DebugLogger.log("Update failed!", Level.INFO);
            return false;
        }
    }

    public String getChangelog(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(updateURL + "change.log").openStream()));
            String inputLine, updatemessage = "";
            ArrayList<String> content = new ArrayList<>();

            while((inputLine = in.readLine()) != null){
                content.add(inputLine);
            }

            for(String n:content){
                updatemessage = updatemessage + n + "\n";
            }
            return updatemessage;

        }catch(Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
            return null;
        }
    }

}
