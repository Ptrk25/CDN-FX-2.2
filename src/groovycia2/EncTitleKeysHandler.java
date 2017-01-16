package groovycia2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncTitleKeysHandler {

    private final int TICKETLEN = 32;

    private ObservableList<Ticket> titlelist;
    private byte[] data;

    private List<Integer> ticketOffsets;
    private List<Integer> apptypeCount;

    public EncTitleKeysHandler(){
        ticketOffsets = new ArrayList<>();
        titlelist = FXCollections.observableArrayList();
        apptypeCount = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);  // es-app, dlp, demo, upd-p, dlc, dsiw, dsisys, dsidat, sys, myst, any
    }

    public ObservableList<Ticket> getTitleList(){
        return titlelist;
    }

    public List<Integer> getApptypeCount(){
        return apptypeCount;
    }

    public void openFile(String path) throws IOException{
        Path realPath = Paths.get(path);
        data = Files.readAllBytes(realPath);
    }

    public void addToTitleList(){
        long datalen = this.data.length;

        for(int i = 16; i < datalen; i += TICKETLEN){
            Ticket ticket = new Ticket();
            ticket.setTitleID(ConvertingTools.bytesToHex(Arrays.copyOfRange(data,i+8 ,i+16)));
            ticket.setTitleKey(ConvertingTools.bytesToHex(Arrays.copyOfRange(data, i+16, i+32)));
            this.titlelist.add(ticket);
        }
    }

    public void sortTitles() throws Exception{
        String titleid;
        String typecheck;

        for(Ticket tiktik:titlelist){
            titleid = tiktik.getTitleID();
            typecheck = titleid.substring(4, 8).toLowerCase();

            if(typecheck.equals("0000")){
                tiktik.setType(Ticket.Type.ESHOP);
                apptypeCount.set(0, apptypeCount.get(0)+1);
            }else if(typecheck.equals("0001")){
                tiktik.setType(Ticket.Type.DLP);
                apptypeCount.set(1, apptypeCount.get(1)+1);
            }else if(typecheck.equals("0002")){
                tiktik.setType(Ticket.Type.DEMO);
                apptypeCount.set(2, apptypeCount.get(2)+1);
            }else if(typecheck.equals("000e")){
                tiktik.setType(Ticket.Type.UPDATE);
                apptypeCount.set(3, apptypeCount.get(3)+1);
            }else if(typecheck.equals("008c")){
                tiktik.setType(Ticket.Type.DLC);
                apptypeCount.set(4, apptypeCount.get(4)+1);
            }else if(typecheck.equals("8004")){
                tiktik.setType(Ticket.Type.DSIWARE);
                apptypeCount.set(5, apptypeCount.get(5)+1);
            }else if(((Long.parseLong(typecheck, 16)) & 0x10) == 0x10){
                tiktik.setType(Ticket.Type.SYSTEM);
                apptypeCount.set(8, apptypeCount.get(8)+1);
            }else if(typecheck.equals("8005")){
                tiktik.setType(Ticket.Type.DSISYSAPP);
                apptypeCount.set(6, apptypeCount.get(6)+1);
            }else if(typecheck.equals("800f")){
                tiktik.setType(Ticket.Type.DSISYSDAT);
                apptypeCount.set(7, apptypeCount.get(7)+1);
            }else{
                tiktik.setType(Ticket.Type.MYSTERY);
                apptypeCount.set(9, apptypeCount.get(9)+1);
            }
        }
    }

}
