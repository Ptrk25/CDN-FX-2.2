package groovycia2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

public class CDNFXController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuItem menuTMOpenTicketDB,
            menuTMSelectOutputFolder,
            menuTDSelectOutputfolder,
            menuClose,
            menuSettings,
            menuWebsite,
            menuThread,
            menuTDSelectTikOutputfolder;

    @FXML
    private Tab tabTM,
            tabTDA,
            tabTDM,
            tabTBV;

    @FXML
    private Label lblTMTicketCount,
            lblTMUniqueTicketCount,
            lblTMDuplicatesTicketCount,
            lblTMSystemTicketCount,
            lblTMEShopTicketCount,
            lblTMUniqueEShopTicketCount,
            lblTMEShopDuplicatesCount,
            lblTMTicketNotFromYourEShopCount,
            lblTMTitleCount,
            lblTMAttemptCount,
            lblTMFailedCount,
            lblTMTitleID,
            lblTMTMD,
            lblTMFilesCount,
            lblTMDownloadStats,
            lblTMTitleName,
            lblTDATotalCount,
            lblTDAeShopCount,
            lblTDADemoCount,
            lblTDAUpdatesCount,
            lblTDADLPCount,
            lblTDADLCCount,
            lblTDADSiWareCount,
            lblTDADSiSysAppsCount,
            lblTDADSiSysDataCount,
            lblTDASystemCount,
            lblTDATitleCount,
            lblTDAAttemptCount,
            lblTDAFailedCount,
            lblTDATitleID,
            lblTDATMD,
            lblTDAFilesCount,
            lblTDADownloadStats,
            lblTDATitleName,
            lblTDMTitleCount,
            lblTDMAttemptCount,
            lblTDMFailedCount,
            lblTDMTitleID,
            lblTDMTMD,
            lblTDMFilesCount,
            lblTDMDownloadStats,
            lblTDVTotalCount,
            lblTDVeShopAppCount,
            lblTDVDemoCount,
            lblTDVUpdatesCount,
            lblTDVDLPCount,
            lblTDVDLCCount,
            lblTDVDSiWareCount,
            lblTDVDSiSysAppsCount,
            lblTDVDSiSysDataCount,
            lblTDVSystemCount,
            lblTDMTitleName;

    @FXML
    private ListView listTMCategory,
            listTDACategory,
            listTDVCategory;

    @FXML
    private TableView tableTMTickets,
            tableTDATickets,
            tableTDVTickets;

    @FXML
    private TableColumn<Ticket, String> columnTMName,
            columnTMRegion,
            columnTMSerial,
            columnTMType,
            columnTMTitleID,
            columnTMConsoleID,
            columnTDAName,
            columnTDARegion,
            columnTDASerial,
            columnTDAType,
            columnTDATitleID,
            columnTDVName,
            columnTDVRegion,
            columnTDVSerial,
            columnTDVType,
            columnTDVTitleID;

    @FXML
    private TableColumn<Ticket, Boolean> columnTMDL,
            columnTDADL;

    @FXML
    private TextField textTMSearch,
            textTDASearch,
            textTDMTitleID,
            textTDMEncryptedTitleKey,
            textTDVSearch;

    @FXML
    private CheckBox chbxTMBuildCIA,
            chbxTMPatchDLC,
            chbxTMPatchDemo,
            chbxTMPersonal,
            chbxTDAGenTickets;

    @FXML
    private Button btnTMDownload,
            btnTDADownload,
            btnTDAOpenFile,
            btnTDMDownload,
            btnTDMGenTicket,
            btnRebuildRawContent,
            btnTDARebuildRawContent,
            btnTDAGenTickets;

    @FXML
    private ProgressBar progressbarTMDownload,
            progressbarTDADownload,
            progressbarTDMDownload;

    @FXML
    private ImageView imgvTMIcon,
            imgvTDAIcon,
            imgvTDMIcon;

    @FXML
    private CheckBox chbxTDABuildCIA,
            chbxTDAPatchDLC,
            chbxTDAPatchDemo,
            chbxTDMBuildCIA,
            chbxTDMPatchDLC,
            chbxTDMPatchDemo;

    private String tmdbpath = "", tmoutputpath = "", tdapath = "", tdaoutputpath = "", tdtikoutputpath = "";
    private ObservableList<Ticket> tmDownloadList = FXCollections.observableArrayList();
    private ObservableList<Ticket> tdaDownloadList = FXCollections.observableArrayList();
    private SortedList<Ticket> tmSortedList;
    private SortedList<Ticket> tdaSortedList;
    private SortedList<Ticket> tdvSortedList;

    private Downloader dl;
    private XMLHandler xml_handler = new XMLHandler(null);

    private MenuItem tmMnuAddAllToDownloadList;
    private MenuItem tmMnuRemoveAllFromDownloadList;

    private MenuItem tdaMnuAddAllToDownloadList;
    private MenuItem tdaMnuRemoveAllFromDownloadList;

    private CDNFXController cdnfxController;

    public void initialize(URL location, ResourceBundle resources){
        XMLUpdater xmlu = new XMLUpdater();
        if(xmlu.checkForUpdates())
            xmlu.update();
        initProperties();
        initDatabaseViewer();
       xml_handler.getXMLFileFromServer();
    }

    public void setCdnfxController(CDNFXController cdnfxController){
        this.cdnfxController = cdnfxController;
    }

    public void initProperties(){
        try{
            PropertiesHandler.createFile();
            if(PropertiesHandler.getTMInputPath() != null && PropertiesHandler.getTMInputPath().length() > 1){
                tmdbpath = PropertiesHandler.getTMInputPath();
                tmOpenTicketDB();
                DebugLogger.log("INIT: Ticket.db opened", Level.INFO);
            }
            if(PropertiesHandler.getTMOutputPath() != null && PropertiesHandler.getTMOutputPath().length() > 1){
                tmoutputpath = PropertiesHandler.getTMOutputPath();
                btnRebuildRawContent.setDisable(false);
                DebugLogger.log("INIT: TM Outputfolder setted", Level.INFO);
            }
            if(PropertiesHandler.getTDAInputPath() != null && PropertiesHandler.getTDAInputPath().length() > 1){
                tdapath = PropertiesHandler.getTDAInputPath();
                tdaOpenFile();
                DebugLogger.log("INIT: TD encTitleKeys.bin opened", Level.INFO);
            }
            if(PropertiesHandler.getTDOutputPath() != null && PropertiesHandler.getTDOutputPath().length() > 1){
                tdaoutputpath = PropertiesHandler.getTDOutputPath();
                btnTDARebuildRawContent.setDisable(false);
                DebugLogger.log("INIT: TD Outputfolder setted", Level.INFO);
            }
            if(PropertiesHandler.getTDTikPath() != null && PropertiesHandler.getTDOutputPath().length() > 1){
                tdtikoutputpath = PropertiesHandler.getTDTikPath();
                DebugLogger.log("INIT: TD .tik Outputfolder setted", Level.INFO);
            }
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    private void initDatabaseViewer(){
        ObservableList<Ticket> titlelist;

        DebugLogger.log("INIT: Init Titledatabase Viewer", Level.INFO);

        titlelist = xml_handler.readCommunityXMLFile(true);
        tdvUpdateCounters(titlelist);

        tableTDVTickets.setEditable(true);
        tableTDVTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //TABLEFILTER
        TDVTableFilter.filteredTickets = new FilteredList<>(titlelist, t -> true);
        tdvSortedList = new SortedList<>(TDVTableFilter.createTableFilter(textTDVSearch, listTDVCategory));
        tdvSortedList.comparatorProperty().bind(tableTDVTickets.comparatorProperty());
        tableTDVTickets.setItems(tdvSortedList);

        if(DetectOS.isMac()){
            columnTDVTitleID.setMinWidth(144.57);
            columnTDVTitleID.setMaxWidth(144.57);
            columnTDVType.setMinWidth(82.57);
            columnTDVType.setMaxWidth(82.57);
            columnTDVSerial.setMinWidth(89.57);
            columnTDVSerial.setMaxWidth(89.57);
            columnTDVRegion.setMinWidth(44.57);
            columnTDVRegion.setMaxWidth(44.57);
            columnTDVName.setPrefWidth(149.57);
            columnTDVName.setMinWidth(149.57);
        }else{
            columnTDVTitleID.setMinWidth(120.0);
            columnTDVTitleID.setMaxWidth(120.0);
            columnTDVType.setMinWidth(80.0);
            columnTDVType.setMaxWidth(80.0);
            columnTDVSerial.setMinWidth(80);
            columnTDVSerial.setMaxWidth(80);
            columnTDVRegion.setMinWidth(50);
            columnTDVRegion.setMaxWidth(50);
        }

        columnTDVName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnTDVRegion.setCellValueFactory(cellData -> cellData.getValue().regionProperty());
        columnTDVSerial.setCellValueFactory(cellData -> cellData.getValue().serialProperty());
        columnTDVType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnTDVTitleID.setCellValueFactory(cellData -> cellData.getValue().titleidProperty());

        columnTDVName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDVRegion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDVSerial.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDVTitleID.setCellFactory(TextFieldTableCell.forTableColumn());

        columnTDVName.setEditable(false);
        columnTDVRegion.setEditable(false);
        columnTDVSerial.setEditable(false);

        columnTDVTitleID.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Ticket, String>>() {
                    public void handle(TableColumn.CellEditEvent<Ticket, String> t) {
                        Ticket ticket = ((Ticket) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        ((Ticket) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTitleID(t.getOldValue());
                    }
                }
        );

        textTDVSearch.setDisable(false);

        DebugLogger.log("INIT: Titledatabase Viewer initialized!", Level.INFO);
    }

    private void tdvUpdateCounters(ObservableList<Ticket> titlelist){
        int i = 0;
        List<Integer> apptypeCount = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);  // es-app, dlp, demo, upd-p, dlc, dsiw, dsisys, dsidat, sys, myst, any

        //SET TYPES
        for(Ticket tiktik: titlelist){
            String titleid = tiktik.getTitleID();
            String typecheck = titleid.substring(4, 8).toLowerCase();

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

        lblTDVTotalCount.setText(Integer.toString(titlelist.size()));
        for(int ticketcount:apptypeCount){
            switch (i){
                case 0:
                    lblTDVeShopAppCount.setText(Integer.toString(ticketcount));
                    break;
                case 1:
                    lblTDVDLPCount.setText(Integer.toString(ticketcount));
                    break;
                case 2:
                    lblTDVDemoCount.setText(Integer.toString(ticketcount));
                    break;
                case 3:
                    lblTDVUpdatesCount.setText(Integer.toString(ticketcount));
                    break;
                case 4:
                    lblTDVDLCCount.setText(Integer.toString(ticketcount));
                    break;
                case 5:
                    lblTDVDSiWareCount.setText(Integer.toString(ticketcount));
                    break;
                case 6:
                    lblTDVDSiSysAppsCount.setText(Integer.toString(ticketcount));
                    break;
                case 7:
                    lblTDVDSiSysDataCount.setText(Integer.toString(ticketcount));
                    break;
                case 8:
                    lblTDVSystemCount.setText(Integer.toString(ticketcount));
                    break;
            }
            i++;
        }

        listTDVCategory.getItems().removeAll();
        listTDVCategory.getItems().add("All");
        listTDVCategory.getItems().add("eShopApp");
        listTDVCategory.getItems().add("DownloadPlayChild");
        listTDVCategory.getItems().add("Demo");
        listTDVCategory.getItems().add("UpdatePatch");
        listTDVCategory.getItems().add("DLC");
        listTDVCategory.getItems().add("DSiWare");
        listTDVCategory.getItems().add("DSiSystemApp");
        listTDVCategory.getItems().add("DSiSystemData");
        listTDVCategory.getItems().add("System");

        listTDVCategory.getSelectionModel().select(0);

    }

    private void tmUpdateCounters(TicketHandler th){
        int i = 0;
        int final_count = 0;
        List<Integer> ticket_c = th.getTicketCount();

        for(int ticketcount:ticket_c){
            switch (i){
                case 0:
                    lblTMTicketCount.setText(Integer.toString(ticketcount));
                    break;
                case 1:
                    lblTMUniqueTicketCount.setText(Integer.toString(ticketcount));
                    break;
                case 2:
                    lblTMDuplicatesTicketCount.setText(Integer.toString(ticketcount));
                    break;
                case 3:
                    lblTMSystemTicketCount.setText(Integer.toString(ticketcount));
                    break;
                case 4:
                    lblTMEShopTicketCount.setText(Integer.toString(ticketcount));
                    break;
                case 5:
                    lblTMUniqueEShopTicketCount.setText(Integer.toString(ticketcount));
                case 6:
                    lblTMEShopDuplicatesCount.setText(Integer.toString(ticketcount));
                case 7:
                    lblTMTicketNotFromYourEShopCount.setText(Integer.toString(ticketcount));
                    break;
            }
            i++;
        }

        List<Integer> apptype = th.getApptypeCount();

        for (int ticketcount:apptype)
            final_count += ticketcount;

        listTMCategory.getItems().clear();

        tmMnuAddAllToDownloadList = new MenuItem("Add all Titles of this category to downloadlist");
        tmMnuRemoveAllFromDownloadList = new MenuItem("Remove all Titles of this category from downloadlist");

        tmMnuAddAllToDownloadList.setOnAction((ActionEvent event) -> {
            String type = "";

            boolean sys, nonunique;
            sys = false;
            nonunique = false;

            tmDownloadList.clear();

            if(PropertiesHandler.getProperties("downloadsystemtitles") != null){
                sys = PropertiesHandler.getProperties("downloadsystemtitles").equals("yes");
            }
            if(PropertiesHandler.getProperties("downloadnonuniquetitles") != null){
                nonunique = PropertiesHandler.getProperties("downloadnonuniquetitles").equals("yes");
            }

            switch (listTMCategory.getSelectionModel().getSelectedIndex()){
                case 0:
                    type = "All";
                    break;
                case 1:
                    type = "eShopApp";
                    break;
                case 2:
                    type = "DownloadPlayChild";
                    break;
                case 3:
                    type = "Demo";
                    break;
                case 4:
                    type = "UpdatePatch";
                    break;
                case 5:
                    type = "DLC";
                    break;
                case 6:
                    type = "DSiWare";
                    break;
                case 7:
                    type = "DSiSystemApp";
                    break;
                case 8:
                    type = "DSiSystemData";
                    break;
                case 9:
                    type = "System";
                    break;
                case 10:
                    type = "Mystery";
                    break;
            }

            for(Ticket ticket:tmSortedList){
                if(type.equals("All")){
                    if(ticket.getType().equals("System")){
                        if(sys){
                            ticket.setDownload(true);
                            tmDownloadList.add(ticket);
                        }
                    }else{
                        if(ticket.getConsoleID().equals("00000000")){
                            if(nonunique){
                                ticket.setDownload(true);
                                tmDownloadList.add(ticket);
                            }
                        }else{
                            ticket.setDownload(true);
                            tmDownloadList.add(ticket);
                        }
                    }
                }else if(type.equals("System")){
                    if(sys){
                        ticket.setDownload(true);
                        tmDownloadList.add(ticket);
                    }
                }else{
                    if(type.equals(ticket.getType())){
                        if(nonunique){
                            ticket.setDownload(true);
                            tmDownloadList.add(ticket);
                        }else{
                            if(!ticket.getConsoleID().equals("00000000")){
                                ticket.setDownload(true);
                                tmDownloadList.add(ticket);
                            }
                        }
                    }
                }
            }
        });

        tmMnuRemoveAllFromDownloadList.setOnAction((ActionEvent event)->{
            String type = "";

            switch (listTMCategory.getSelectionModel().getSelectedIndex()){
                case 0:
                    type = "All";
                    break;
                case 1:
                    type = "eShopApp";
                    break;
                case 2:
                    type = "DownloadPlayChild";
                    break;
                case 3:
                    type = "Demo";
                    break;
                case 4:
                    type = "UpdatePatch";
                    break;
                case 5:
                    type = "DLC";
                    break;
                case 6:
                    type = "DSiWare";
                    break;
                case 7:
                    type = "DSiSystemApp";
                    break;
                case 8:
                    type = "DSiSystemData";
                    break;
                case 9:
                    type = "System";
                    break;
                case 10:
                    type = "Mystery";
                    break;
            }

            for(Ticket ticket:tmSortedList){
                if(type.equals(ticket.getType())){
                    tmDownloadList.remove(ticket);
                    ticket.setDownload(false);
                }else if(type.equals("All")){
                    tmDownloadList.remove(ticket);
                    ticket.setDownload(false);
                }
            }
        });

        listTMCategory.setContextMenu(new ContextMenu(tmMnuAddAllToDownloadList, tmMnuRemoveAllFromDownloadList));
        listTMCategory.getItems().add("All (" + final_count + ")");
        i = 0;

        for(int apptypecount:apptype){
            switch(i){
                case 0:
                    listTMCategory.getItems().add("eShopApp (" + apptypecount + ")");
                    break;
                case 1:
                    listTMCategory.getItems().add("DownloadPlayChild (" + apptypecount + ")");
                    break;
                case 2:
                    listTMCategory.getItems().add("Demo (" + apptypecount + ")");
                    break;
                case 3:
                    listTMCategory.getItems().add("UpdatePatch (" + apptypecount + ")");
                    break;
                case 4:
                    listTMCategory.getItems().add("DLC (" + apptypecount + ")");
                    break;
                case 5:
                    listTMCategory.getItems().add("DSiWare (" + apptypecount + ")");
                    break;
                case 6:
                    listTMCategory.getItems().add("DSiSystemApp (" + apptypecount + ")");
                    break;
                case 7:
                    listTMCategory.getItems().add("DSiSystemData (" + apptypecount + ")");
                    break;
                case 8:
                    listTMCategory.getItems().add("System (" + apptypecount + ")");
                    break;
                case 9:
                    listTMCategory.getItems().add("Mystery (" + apptypecount + ")");
                    break;
            }
            i++;
        }
        listTMCategory.getSelectionModel().select(0);
    }

    private void tdaUpdateCounters(EncTitleKeysHandler ehandler){
        int i = 0;
        int final_count = 0;
        List<Integer> apptype = ehandler.getApptypeCount();

        for (int titlecount:apptype)
            final_count += titlecount;

        listTDACategory.getItems().clear();

        tdaMnuAddAllToDownloadList = new MenuItem("Add all Titles of this category to downloadlist");
        tdaMnuRemoveAllFromDownloadList = new MenuItem("Remove all Titles of this category from downloadlist");

        tdaMnuAddAllToDownloadList.setOnAction((ActionEvent event) ->{
            String type = "";
            tdaDownloadList.clear();

            switch (listTDACategory.getSelectionModel().getSelectedIndex()){
                case 0:
                    type = "All";
                    break;
                case 1:
                    type = "eShopApp";
                    break;
                case 2:
                    type = "DownloadPlayChild";
                    break;
                case 3:
                    type = "Demo";
                    break;
                case 4:
                    type = "UpdatePatch";
                    break;
                case 5:
                    type = "DLC";
                    break;
                case 6:
                    type = "DSiWare";
                    break;
                case 7:
                    type = "DSiSystemApp";
                    break;
                case 8:
                    type = "DSiSystemData";
                    break;
                case 9:
                    type = "System";
                    break;
                case 10:
                    type = "Mystery";
                    break;
            }

            for(Ticket title:tdaSortedList){
                if(type.equals("All")){
                    title.setDownload(true);
                    tdaDownloadList.add(title);
                }else{
                    if(type.equals(title.getType())){
                        title.setDownload(true);
                        tdaDownloadList.add(title);
                    }
                }
            }
        });

        tdaMnuRemoveAllFromDownloadList.setOnAction((ActionEvent event) ->{
            String type = "";

            switch (listTDACategory.getSelectionModel().getSelectedIndex()){
                case 0:
                    type = "All";
                    break;
                case 1:
                    type = "eShopApp";
                    break;
                case 2:
                    type = "DownloadPlayChild";
                    break;
                case 3:
                    type = "Demo";
                    break;
                case 4:
                    type = "UpdatePatch";
                    break;
                case 5:
                    type = "DLC";
                    break;
                case 6:
                    type = "DSiWare";
                    break;
                case 7:
                    type = "DSiSystemApp";
                    break;
                case 8:
                    type = "DSiSystemData";
                    break;
                case 9:
                    type = "System";
                    break;
                case 10:
                    type = "Mystery";
                    break;
            }

            for(Ticket title:tdaSortedList){
                if(type.equals(title.getType())){
                    tdaDownloadList.remove(title);
                    title.setDownload(false);
                }else if(type.equals("All")){
                    tdaDownloadList.remove(title);
                    title.setDownload(false);
                }
            }

        });

        lblTDATotalCount.setText(Integer.toString(ehandler.getTitleList().size()));
        for(int ticketcount:apptype){
            switch (i){
                case 0:
                    lblTDAeShopCount.setText(Integer.toString(ticketcount));
                    break;
                case 1:
                    lblTDADLPCount.setText(Integer.toString(ticketcount));
                    break;
                case 2:
                    lblTDADemoCount.setText(Integer.toString(ticketcount));
                    break;
                case 3:
                    lblTDAUpdatesCount.setText(Integer.toString(ticketcount));
                    break;
                case 4:
                    lblTDADLCCount.setText(Integer.toString(ticketcount));
                    break;
                case 5:
                    lblTDADSiWareCount.setText(Integer.toString(ticketcount));
                    break;
                case 6:
                    lblTDADSiSysAppsCount.setText(Integer.toString(ticketcount));
                    break;
                case 7:
                    lblTDADSiSysDataCount.setText(Integer.toString(ticketcount));
                    break;
                case 8:
                    lblTDASystemCount.setText(Integer.toString(ticketcount));
                    break;
            }
            i++;
        }

        listTDACategory.setContextMenu(new ContextMenu(tdaMnuAddAllToDownloadList, tdaMnuRemoveAllFromDownloadList));

        listTDACategory.getItems().removeAll();
        listTDACategory.getItems().add("All");
        listTDACategory.getItems().add("eShopApp");
        listTDACategory.getItems().add("DownloadPlayChild");
        listTDACategory.getItems().add("Demo");
        listTDACategory.getItems().add("UpdatePatch");
        listTDACategory.getItems().add("DLC");
        listTDACategory.getItems().add("DSiWare");
        listTDACategory.getItems().add("DSiSystemApp");
        listTDACategory.getItems().add("DSiSystemData");
        listTDACategory.getItems().add("System");

        listTDACategory.getSelectionModel().select(0);

    }

    private void tmOpenTicketDB() throws Exception{
        TicketHandler thandler = new TicketHandler();
        ObservableList<Ticket> ticketlist;

        thandler.openFile(tmdbpath);
        thandler.addToTicketList();
        thandler.countTickets();
        thandler.sortTickets();

        //DATABASE
        xml_handler.setTicketList(thandler.getTicketList());
        ticketlist = xml_handler.readCommunityXMLFile(false);

        if(ticketlist == null){
            ticketlist = thandler.getTicketList();
        }

        tmUpdateCounters(thandler);

        tableTMTickets.setEditable(true);
        tableTMTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //TABLEFILTER
        TMTableFilter.filteredTickets = new FilteredList<>(ticketlist, t -> true);
        tmSortedList = new SortedList<>(TMTableFilter.createTableFilter(textTMSearch, listTMCategory));
        tmSortedList.comparatorProperty().bind(tableTMTickets.comparatorProperty());
        tableTMTickets.setItems(tmSortedList);

        //DOUBLECLICK
        tableTMTickets.setOnMouseClicked((MouseEvent click) ->{
            Ticket ticket = tmSortedList.get(tableTMTickets.getSelectionModel().getSelectedIndex());
            if(click.getClickCount() == 2){
                if(!ticket.getDownload()){
                    boolean sys, nonunique;
                    sys = false;
                    nonunique = false;

                    if(PropertiesHandler.getProperties("downloadsystemtitles") != null){
                        sys = PropertiesHandler.getProperties("downloadsystemtitles").equals("yes");
                    }
                    if(PropertiesHandler.getProperties("downloadnonuniquetitles") != null){
                        nonunique = PropertiesHandler.getProperties("downloadnonuniquetitles").equals("yes");
                    }

                    if(ticket.getType().equals("System")){
                        if(sys){
                            ticket.setDownload(true);
                            tmDownloadList.add(ticket);
                        }
                    }else{
                        if(ticket.getConsoleID().equals("00000000")){
                            if(nonunique){
                                ticket.setDownload(true);
                                tmDownloadList.add(ticket);
                            }
                        }else{
                            ticket.setDownload(true);
                            tmDownloadList.add(ticket);
                        }
                    }
                }else{
                    if(tmDownloadList.contains(ticket)){
                        tmDownloadList.remove(ticket);
                        ticket.setDownload(false);
                    }
                }
            }

        });

        if(DetectOS.isMac()){
            columnTMConsoleID.setMinWidth(81.57);
            columnTMConsoleID.setMaxWidth(81.57);
            columnTMTitleID.setMinWidth(144.57);
            columnTMTitleID.setMaxWidth(144.57);
            columnTMType.setMinWidth(82.57);
            columnTMType.setMaxWidth(82.57);
            columnTMSerial.setMinWidth(89.57);
            columnTMSerial.setMaxWidth(89.57);
            columnTMRegion.setMinWidth(44.57);
            columnTMRegion.setMaxWidth(44.57);
            columnTMDL.setMaxWidth(25.57);
            columnTMDL.setMinWidth(25.57);
            columnTMName.setPrefWidth(149.57);
            columnTMName.setMinWidth(149.57);
        }else{
            columnTMConsoleID.setMinWidth(80.0);
            columnTMConsoleID.setMaxWidth(80.0);
            columnTMTitleID.setMinWidth(120.0);
            columnTMTitleID.setMaxWidth(120.0);
            columnTMType.setMinWidth(80.0);
            columnTMType.setMaxWidth(80.0);
            columnTMSerial.setMinWidth(80);
            columnTMSerial.setMaxWidth(80);
            columnTMRegion.setMinWidth(50);
            columnTMRegion.setMaxWidth(50);
            columnTMDL.setMaxWidth(20);
            columnTMDL.setMinWidth(20);
        }

        columnTMName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnTMRegion.setCellValueFactory(cellData -> cellData.getValue().regionProperty());
        columnTMSerial.setCellValueFactory(cellData -> cellData.getValue().serialProperty());
        columnTMType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnTMTitleID.setCellValueFactory(cellData -> cellData.getValue().titleidProperty());
        columnTMConsoleID.setCellValueFactory(cellData -> cellData.getValue().consoleidProperty());
        columnTMDL.setCellValueFactory(cellData -> cellData.getValue().downloadProperty().asObject());

        columnTMName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTMRegion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTMSerial.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTMTitleID.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTMDL.setCellFactory(column -> {
            return new TableCell<Ticket, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(null);

                    TableRow<Ticket> currentRow = getTableRow();

                    HBox box= new HBox();
                    box.setSpacing(0);
                    ImageView imageview = new ImageView();
                    imageview.setFitHeight(14);
                    imageview.setFitWidth(14);
                    imageview.setImage(new Image(Main.class.getResource("/resources/success-icon.png").toString()));
                    box.getChildren().add(imageview);
                    setGraphic(null);

                    if (!isEmpty()) {
                        if(getItem().toString() == "true"){
                            setGraphic(box);
                        }
                    }
                }
            };
        });

        columnTMName.setEditable(false);
        columnTMRegion.setEditable(false);
        columnTMSerial.setEditable(false);

        columnTMTitleID.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Ticket, String>>() {
                    public void handle(TableColumn.CellEditEvent<Ticket, String> t) {
                        Ticket ticket = ((Ticket) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        ((Ticket) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTitleID(t.getOldValue());
                    }
                }
        );

        textTMSearch.setDisable(false);
        btnTMDownload.setDisable(false);
    }

    private void tdaOpenFile() throws Exception{
        EncTitleKeysHandler ehandler = new EncTitleKeysHandler();
        ObservableList<Ticket> titlelist;

        ehandler.openFile(tdapath);
        ehandler.addToTitleList();
        ehandler.sortTitles();

        //DATABASE
        xml_handler.setTicketList(ehandler.getTitleList());
        titlelist = xml_handler.readXMLFile();
        titlelist = xml_handler.readCommunityXMLFile(false);


        if(titlelist == null)
            titlelist = ehandler.getTitleList();

        tdaUpdateCounters(ehandler);

        tableTDATickets.setEditable(true);
        tableTDATickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //TABLEFILTER
        TDATableFilter.filteredTickets = new FilteredList<>(titlelist, t -> true);
        tdaSortedList = new SortedList<>(TDATableFilter.createTableFilter(textTDASearch, listTDACategory));
        tdaSortedList.comparatorProperty().bind(tableTDATickets.comparatorProperty());
        tableTDATickets.setItems(tdaSortedList);

        //DOUBLECLICK
        tableTDATickets.setOnMouseClicked((MouseEvent click)->{
            Ticket ticket = tdaSortedList.get(tableTDATickets.getSelectionModel().getSelectedIndex());
            if(click.getClickCount() == 2){
                if(!ticket.getDownload()){
                    ticket.setDownload(true);
                    tdaDownloadList.add(ticket);
                }else{
                    if(tdaDownloadList.contains(ticket)){
                        tdaDownloadList.remove(ticket);
                        ticket.setDownload(false);
                    }
                }
            }
        });

        if(DetectOS.isMac()){
            columnTDATitleID.setMinWidth(144.57);
            columnTDATitleID.setMaxWidth(144.57);
            columnTDAType.setMinWidth(82.57);
            columnTDAType.setMaxWidth(82.57);
            columnTDASerial.setMinWidth(89.57);
            columnTDASerial.setMaxWidth(89.57);
            columnTDARegion.setMinWidth(44.57);
            columnTDARegion.setMaxWidth(44.57);
            columnTDADL.setMaxWidth(25.57);
            columnTDADL.setMinWidth(25.57);
            columnTDAName.setPrefWidth(149.57);
            columnTDAName.setMinWidth(149.57);
        }else{
            columnTDATitleID.setMinWidth(120.0);
            columnTDATitleID.setMaxWidth(120.0);
            columnTDAType.setMinWidth(80.0);
            columnTDAType.setMaxWidth(80.0);
            columnTDASerial.setMinWidth(80);
            columnTDASerial.setMaxWidth(80);
            columnTDARegion.setMinWidth(50);
            columnTDARegion.setMaxWidth(50);
            columnTDADL.setMaxWidth(20);
            columnTDADL.setMinWidth(20);
        }

        columnTDAName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnTDARegion.setCellValueFactory(cellData -> cellData.getValue().regionProperty());
        columnTDASerial.setCellValueFactory(cellData -> cellData.getValue().serialProperty());
        columnTDAType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnTDATitleID.setCellValueFactory(cellData -> cellData.getValue().titleidProperty());
        columnTDADL.setCellValueFactory(cellData -> cellData.getValue().downloadProperty().asObject());

        columnTDAName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDARegion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDASerial.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDATitleID.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTDADL.setCellFactory(column -> {
            return new TableCell<Ticket, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(null);

                    TableRow<Ticket> currentRow = getTableRow();

                    HBox box= new HBox();
                    box.setSpacing(0);
                    ImageView imageview = new ImageView();
                    imageview.setFitHeight(14);
                    imageview.setFitWidth(14);
                    imageview.setImage(new Image(Main.class.getResource("/resources/success-icon.png").toString()));
                    box.getChildren().add(imageview);
                    setGraphic(null);

                    if (!isEmpty()) {
                        if(getItem().toString() == "true"){
                            setGraphic(box);
                        }
                    }
                }
            };
        });

        columnTDAName.setEditable(false);
        columnTDARegion.setEditable(false);
        columnTDASerial.setEditable(false);

        columnTDATitleID.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Ticket, String>>() {
                    public void handle(TableColumn.CellEditEvent<Ticket, String> t) {
                        Ticket ticket = ((Ticket) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                        ((Ticket) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTitleID(t.getOldValue());
                    }
                }
        );

        textTDASearch.setDisable(false);
        btnTDAGenTickets.setDisable(false);
        btnTDADownload.setDisable(false);
    }

    //FUNCTIONS
    @FXML
    protected void btnCIATClickedStart() {

    }

    @FXML
    protected void btnCIATPatchClickedCIA() {

    }

    @FXML
    protected void btnCIATPatchClickedIcon() {

    }

    @FXML
    protected void btnCIATPatchClickedManual() {

    }

    @FXML
    protected void btnCIATRepackClickedCIAOutput() {

    }

    @FXML
    protected void btnCIATRepackClickedInputFolder() {

    }

    @FXML
    protected void btnCIATUnpackClickedCIA() {

    }

    @FXML
    protected void btnCIATUnpackClickedOutputFolder() {

    }

    @FXML
    protected void btnTDAClickedDownload() {
        if(btnTDADownload.getText().equals("Download")){
            if(tdaDownloadList.size() > 0){
                if(tdaoutputpath.length() > 0){
                    btnTDADownload.setText("Cancel");
                    btnTDAGenTickets.setDisable(true);
                    btnTDARebuildRawContent.setDisable(true);
                    tableTDATickets.setEditable(false);
                    tdaMnuAddAllToDownloadList.setDisable(true);
                    tdaMnuRemoveAllFromDownloadList.setDisable(true);
                    textTDASearch.setDisable(true);
                    //TABS
                    tabTM.setDisable(true);
                    tabTDM.setDisable(true);

                    dl = new Downloader(tdaDownloadList, tdaoutputpath, false);
                    dl.setPatchDemo(chbxTDAPatchDemo.isSelected());
                    dl.setPatchDLC(chbxTDAPatchDLC.isSelected());
                    dl.setBlankID(false);
                    dl.setComponents(lblTDATitleCount, lblTDAAttemptCount, lblTDAFailedCount, lblTDATitleName, lblTDATitleID, lblTDATMD, lblTDAFilesCount, lblTDADownloadStats, progressbarTDADownload, btnTDADownload, imgvTDAIcon);
                    dl.setXtraComponents(btnTDARebuildRawContent, tdaMnuAddAllToDownloadList, tdaMnuRemoveAllFromDownloadList, textTDASearch, tableTDATickets);
                    dl.setTabs(tabTM,tabTDA, tabTDM);
                    dl.setGenComponent(btnTDAGenTickets);
                    dl.start();
                }else{
                    Alert warning = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    warning.setTitle("Error");
                    warning.setHeaderText("Outputfolder!");
                    warning.setContentText("Please choose an outputfolder first!");
                    warning.showAndWait();
                }
            }else{
                Alert warning = new Alert(Alert.AlertType.ERROR);
                Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/resources/gciaicon.png"));
                warning.setTitle("Error");
                warning.setHeaderText("No Titles selected!");
                warning.setContentText("Please select at least one title!");
                warning.showAndWait();
            }
        }else{
            dl.setInterrupted(true);
            btnTDADownload.setText("Download");
            btnTDARebuildRawContent.setDisable(false);
            btnTDAGenTickets.setDisable(false);
            tableTDATickets.setEditable(true);
            tdaMnuAddAllToDownloadList.setDisable(false);
            tdaMnuRemoveAllFromDownloadList.setDisable(false);
            textTDASearch.setDisable(false);
            tabTM.setDisable(false);
            tabTDM.setDisable(false);
            //tabCIAT.setDisable(false);
            //tabSocketPunch.setDisable(false);

            try{
                InputStream stream = Main.class.getResourceAsStream("/resources/icon.png");
                BufferedImage icon_b = ImageIO.read(stream);
                Image icon = SwingFXUtils.toFXImage(icon_b, null);
                imgvTDAIcon.setImage(icon);
            }catch (Exception e){
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                DebugLogger.log(errors.toString(), Level.SEVERE);
            }

            DebugLogger.log("Download cancled!", Level.INFO);
        }
    }

    @FXML
    protected void btnTDAClickedOpenFile() throws Exception{
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(1, path.lastIndexOf("/")) + "/";
        //FILECHOOSER
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open encTitleKeys.bin");
        fileChooser.setInitialDirectory(new File(path));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("encTitleKeys.bin", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        if(selectedFile != null){
            tdapath = selectedFile.getPath();
            tdaOpenFile();
            DebugLogger.log("encTitleKeys.bin opened", Level.INFO);
        }
    }

    @FXML
    protected void btnTDMClickedDownload() {
        if(btnTDMDownload.getText().equals("Download")){
            if(textTDMTitleID.getText().length() == 16 && textTDMEncryptedTitleKey.getText().length() == 32){
                if(textTDMTitleID.getText().toUpperCase().matches("[0-9A-F]+") && textTDMEncryptedTitleKey.getText().toUpperCase().matches("[0-9A-F]+")){
                    if(tdaoutputpath.length() > 0){
                        btnTDMDownload.setText("Cancel");
                        btnTDMGenTicket.setDisable(true);
                        btnRebuildRawContent.setDisable(true);
                        //TABS
                        tabTM.setDisable(true);
                        tabTDA.setDisable(true);

                        ObservableList<Ticket> item = FXCollections.observableArrayList();
                        Ticket ticket = new Ticket();
                        ticket.setTitleID(textTDMTitleID.getText().toUpperCase());
                        ticket.setTitleKey(textTDMEncryptedTitleKey.getText().toUpperCase());
                        item.add(ticket);
                        xml_handler.setTicketList(item);
                        item = xml_handler.readCommunityXMLFile(false);

                        for(Ticket tiktik:item){
                            String titleid = tiktik.getTitleID();
                            String typecheck = titleid.substring(4, 8).toLowerCase();

                            if(typecheck.equals("0000")){
                                tiktik.setType(Ticket.Type.ESHOP);
                            }else if(typecheck.equals("0001")){
                                tiktik.setType(Ticket.Type.DLP);
                            }else if(typecheck.equals("0002")){
                                tiktik.setType(Ticket.Type.DEMO);
                            }else if(typecheck.equals("000e")){
                                tiktik.setType(Ticket.Type.UPDATE);
                            }else if(typecheck.equals("008c")){
                                tiktik.setType(Ticket.Type.DLC);
                            }else if(typecheck.equals("8004")){
                                tiktik.setType(Ticket.Type.DSIWARE);
                            }else if(((Long.parseLong(typecheck, 16)) & 0x10) == 0x10){
                                tiktik.setType(Ticket.Type.SYSTEM);
                            }else if(typecheck.equals("8005")){
                                tiktik.setType(Ticket.Type.DSISYSAPP);
                            }else if(typecheck.equals("800f")){
                                tiktik.setType(Ticket.Type.DSISYSDAT);
                            }else{
                                tiktik.setType(Ticket.Type.MYSTERY);
                            }
                        }

                        dl = new Downloader(item, tdaoutputpath, false);
                        dl.setPatchDemo(chbxTDMPatchDemo.isSelected());
                        dl.setPatchDLC(chbxTDMPatchDemo.isSelected());
                        dl.setBlankID(false);
                        dl.setComponents(lblTDMTitleCount, lblTDMAttemptCount, lblTDMFailedCount, lblTDMTitleName, lblTDMTitleID, lblTDMTMD, lblTDMFilesCount, lblTDMDownloadStats, progressbarTDMDownload, btnTDMDownload, imgvTDMIcon);
                        dl.setXtraComponents(btnRebuildRawContent, null, null, null, null);
                        dl.setTabs(tabTM,tabTDA, tabTDM);
                        dl.setGenComponent(btnTDMGenTicket);
                        dl.start();
                    }
                    else{
                        Alert warning = new Alert(Alert.AlertType.ERROR);
                        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image("/resources/gciaicon.png"));
                        warning.setTitle("Error");
                        warning.setHeaderText("Outputfolder!");
                        warning.setContentText("Please choose an outputfolder first!");
                        warning.showAndWait();
                    }
                }else{
                    Alert warning = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    warning.setTitle("Error");
                    warning.setHeaderText("Illegal characters!");
                    warning.setContentText("The TitleID or TitleKey contains illeagal characters!");
                    warning.showAndWait();
                }
            }else{
                Alert warning = new Alert(Alert.AlertType.ERROR);
                Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/resources/gciaicon.png"));
                warning.setTitle("Error");
                warning.setHeaderText("Invalid TitleID or TitleKey");
                warning.setContentText("The TitleID or TitleKey is invalid!");
                warning.showAndWait();
            }
        }else{
            dl.setInterrupted(true);
            btnTDMDownload.setText("Download");
            btnRebuildRawContent.setDisable(false);
            btnTDMGenTicket.setDisable(false);
            tabTM.setDisable(false);
            tabTDA.setDisable(false);

            try{
                InputStream stream = Main.class.getResourceAsStream("/resources/icon.png");
                BufferedImage icon_b = ImageIO.read(stream);
                Image icon = SwingFXUtils.toFXImage(icon_b, null);
                imgvTDMIcon.setImage(icon);
            }catch (Exception e){
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                DebugLogger.log(errors.toString(), Level.SEVERE);
            }

            DebugLogger.log("Download cancled!", Level.INFO);
        }
    }

    @FXML
    protected void btnTMClickedDownload() {
        if(btnTMDownload.getText().equals("Download")){
            if(tmDownloadList.size() > 0){
                if(tmoutputpath.length() > 0){
                    btnTMDownload.setText("Cancel");
                    btnRebuildRawContent.setDisable(true);
                    tableTMTickets.setEditable(false);
                    tmMnuAddAllToDownloadList.setDisable(true);
                    tmMnuRemoveAllFromDownloadList.setDisable(true);
                    textTMSearch.setDisable(true);
                    //DISABLE TABS TOO
                    tabTDA.setDisable(true);
                    tabTDM.setDisable(true);

                    dl = new Downloader(tmDownloadList, tmoutputpath, true);
                    dl.setBuildCIA(chbxTMBuildCIA.isSelected());
                    dl.setPatchDemo(chbxTMPatchDemo.isSelected());
                    dl.setPatchDLC(chbxTMPatchDLC.isSelected());
                    dl.setBlankID(!chbxTMPersonal.isSelected());
                    dl.setComponents(lblTMTitleCount, lblTMAttemptCount, lblTMFailedCount, lblTMTitleName, lblTMTitleID, lblTMTMD, lblTMFilesCount, lblTMDownloadStats, progressbarTMDownload, btnTMDownload, imgvTMIcon);
                    dl.setXtraComponents(btnRebuildRawContent, tmMnuAddAllToDownloadList, tmMnuRemoveAllFromDownloadList, textTMSearch, tableTMTickets);
                    dl.setTabs(tabTM,tabTDA, tabTDM);
                    dl.start();
                }else{
                    Alert warning = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    warning.setTitle("Error");
                    warning.setHeaderText("Outputfolder!");
                    warning.setContentText("Please choose an outputfolder first!");
                    warning.showAndWait();
                }
            }else{
                Alert warning = new Alert(Alert.AlertType.ERROR);
                Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/resources/gciaicon.png"));
                warning.setTitle("Error");
                warning.setHeaderText("No Titles selected!");
                warning.setContentText("Please select at least one title!");
                warning.showAndWait();
            }
        }else{
            dl.setInterrupted(true);
            btnTMDownload.setText("Download");
            btnRebuildRawContent.setDisable(false);
            tableTMTickets.setEditable(true);
            tmMnuAddAllToDownloadList.setDisable(false);
            tmMnuRemoveAllFromDownloadList.setDisable(false);
            textTMSearch.setDisable(false);
            tabTDA.setDisable(false);
            tabTDM.setDisable(false);
            //tabCIAT.setDisable(false);
            //tabSocketPunch.setDisable(false);

            try{
                InputStream stream = Main.class.getResourceAsStream("/resources/icon.png");
                BufferedImage icon_b = ImageIO.read(stream);
                Image icon = SwingFXUtils.toFXImage(icon_b, null);
                imgvTMIcon.setImage(icon);
            }catch (Exception e){
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                DebugLogger.log(errors.toString(), Level.SEVERE);
            }

            DebugLogger.log("Download cancled!", Level.INFO);
        }
    }

    @FXML
    protected void btnClickedRebuildRawContent(){
        Alert warning = new Alert(Alert.AlertType.WARNING);
        Stage stage2 = (Stage)warning.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Warning");
        warning.setHeaderText("Rebuild raw content");
        warning.setContentText("This function will ignore the settings how the CIAs will be build. All produced CIAs will only have a TitleID!");
        warning.showAndWait();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RebuildCIA.fxml"));
            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Rebuild raw content");
            stage.setScene(new Scene(root2));
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    // consume event
                    event.consume();
                }
            });
            RebuildCIAController rb = fxmlLoader.getController();
            rb.setInput(tmoutputpath);
            stage.show();
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    @FXML
    protected void btnTDAClickedRebuildRawContent(){
        Alert warning = new Alert(Alert.AlertType.WARNING);
        Stage stage2 = (Stage)warning.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Warning");
        warning.setHeaderText("Rebuild raw content");
        warning.setContentText("This function will ignore the settings how the CIAs will be build. All produced CIAs will only have a TitleID!");
        warning.showAndWait();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RebuildCIA.fxml"));
            Parent root2 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Rebuild raw content");
            stage.setScene(new Scene(root2));
            stage.setResizable(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    // consume event
                    event.consume();
                }
            });
            RebuildCIAController rb = fxmlLoader.getController();
            rb.setInput(tdaoutputpath);
            stage.show();
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    @FXML
    protected void btnTDAClickedGenTickets(){
        if(tdtikoutputpath.length() > 0){
            if(tdaDownloadList.size() > 0){
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/TicketGenerator.fxml"));
                    Parent root2 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setTitle("Generating Tickets");
                    stage.setScene(new Scene(root2));
                    stage.setResizable(false);

                    TicketGeneratorController rb = fxmlLoader.getController();

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            Stage stage2 = (Stage)alert.getDialogPane().getScene().getWindow();
                            stage2.getIcons().add(new Image("/resources/gciaicon.png"));
                            alert.setTitle("Cancel");
                            alert.setHeaderText("Stop Ticket Generation");
                            alert.setContentText("Do you want to stop the Ticket Generation?");

                            alert.initOwner(stage);

                            ButtonType Yes = new ButtonType("Yes");
                            ButtonType No = new ButtonType("No");
                            alert.getButtonTypes().setAll(Yes, No);

                            Optional<ButtonType> result = alert.showAndWait();

                            if (result.get() == Yes){
                                rb.interrupt();
                            }
                            event.consume();
                        }
                    });
                    rb.setInput(tdtikoutputpath, tdaDownloadList);
                    stage.show();
                }catch (Exception e){
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    DebugLogger.log(errors.toString(), Level.SEVERE);
                }
            }else{
                Alert warning = new Alert(Alert.AlertType.ERROR);
                Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/resources/gciaicon.png"));
                warning.setTitle("Error");
                warning.setHeaderText("No Titles selected!");
                warning.setContentText("Please select at least one title!");
                warning.showAndWait();
            }
        }else{
            Alert warning = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Error");
            warning.setHeaderText("Outputfolder!");
            warning.setContentText("Please choose an .tik outputfolder first!");
            warning.showAndWait();
        }
    }

    @FXML
    protected void chbxTDASelectedDLC() {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Information");
        warning.setHeaderText("Patch DLC");
        warning.setContentText("By selecting this option, all DLC content on CDN will be unlocked, regardless of whether it was bought on eShop or not.\n\nDeselecting this option will only download your legit content.");
        warning.showAndWait();
    }

    @FXML
    protected void chbxTDASelectedDemo() {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Information");
        warning.setHeaderText("Patch Demo");
        warning.setContentText("By selecting this option, the demo play count limit will be removed.\n\nDeselecting this option will download this demo without patching anything.");
        warning.showAndWait();
    }

    @FXML
    protected void chbxTDMSelectedDLC() {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Information");
        warning.setHeaderText("Patch DLC");
        warning.setContentText("By selecting this option, all DLC content on CDN will be unlocked, regardless of whether it was bought on eShop or not.\n\nDeselecting this option will only download your legit content.");
        warning.showAndWait();
    }

    @FXML
    protected void chbxTDMSelectedDemo() {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Information");
        warning.setHeaderText("Patch Demo");
        warning.setContentText("By selecting this option, the demo play count limit will be removed.\n\nDeselecting this option will download this demo without patching anything.");
        warning.showAndWait();
    }

    @FXML
    protected void chbxTMSelectedDLC() {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Information");
        warning.setHeaderText("Patch DLC");
        warning.setContentText("By selecting this option, all DLC content on CDN will be unlocked, regardless of whether it was bought on eShop or not.\n\nDeselecting this option will only download your legit content.");
        warning.showAndWait();
    }

    @FXML
    protected void chbxTMSelectedDemo() {
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Information");
        warning.setHeaderText("Patch Demo");
        warning.setContentText("By selecting this option, the demo play count limit will be removed.\n\nDeselecting this option will download this demo without patching anything.");
        warning.showAndWait();
    }

    @FXML
    protected void chbxTMSelectedPersonal() {
        Alert warning = new Alert(Alert.AlertType.WARNING);
        Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/resources/gciaicon.png"));
        warning.setTitle("Warning");
        warning.setHeaderText("Create personal CIAs");
        warning.setContentText("Don't use this if you want to install the CIAs on other systems!\n\nNOTE: Doesn't work with current Firmwares/CFW, CIA won't be installable!");
        if(chbxTMPersonal.isSelected()){
            warning.showAndWait();
        }
    }

    @FXML
    protected void close() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    protected void menuClickedSelectOutput() throws Exception{
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(1, path.lastIndexOf("/")) + "/";
        //DIRECTORYCHOOSER
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(path));
        File selectedDirectory = directoryChooser.showDialog(mainPane.getScene().getWindow());
        if(selectedDirectory != null){
            tmoutputpath = selectedDirectory.getPath();
            btnRebuildRawContent.setDisable(false);
            DebugLogger.log("TM: Outputfolder setted", Level.INFO);
        }
    }

    @FXML
    protected void menuTDClickedSelectOutputfolder() throws Exception{
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(1, path.lastIndexOf("/")) + "/";
        //DIRECTORYCHOOSER
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(path));
        File selectedDirectory = directoryChooser.showDialog(mainPane.getScene().getWindow());
        if(selectedDirectory != null){
            tdaoutputpath = selectedDirectory.getPath();
            btnTDAGenTickets.setDisable(false);
            btnTDARebuildRawContent.setDisable(false);
            DebugLogger.log("TD: Outputfolder setted", Level.INFO);
        }
    }

    @FXML
    protected void menuClickedSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Settings.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Settings");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();

            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setCdnfxController(cdnfxController);
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    @FXML
    protected void menuClickedThread() {
        try {
            Desktop.getDesktop().browse(new URI("http://gbatemp.net/threads/release-groovycia.414004/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            StringWriter errors = new StringWriter();
            e1.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    @FXML
    protected void menuClickedWebsite() {
        try {
            Desktop.getDesktop().browse(new URI("http://ptrk25.github.io/GroovyFX/"));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e1) {
            StringWriter errors = new StringWriter();
            e1.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }

    @FXML
    protected void menuTMClickedOpenTicket() throws Exception{
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(1, path.lastIndexOf("/")) + "/";
        //FILECHOOSER
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open ticket.db");
        fileChooser.setInitialDirectory(new File(path));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ticket.db", "*.db"));
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        if(selectedFile != null){
            tmdbpath = selectedFile.getPath();
            tmOpenTicketDB();
            DebugLogger.log("Ticket.db opened", Level.INFO);
        }
    }

    @FXML
    protected void menuTDClickedSelectTikOutputfolder() throws Exception{
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path = path.substring(1, path.lastIndexOf("/")) + "/";
        //DIRECTORYCHOOSER
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(path));
        File selectedDirectory = directoryChooser.showDialog(mainPane.getScene().getWindow());
        if(selectedDirectory != null){
            tdtikoutputpath = selectedDirectory.getPath();
            DebugLogger.log("TD: Outputfolder setted (.tik)", Level.INFO);
        }
    }

    @FXML
    protected void textTDAClickedEnter() {

    }

    @FXML
    protected void textTDVClickedEnter() {

    }

    @FXML
    protected void textTMClickedEnter() {

    }

    @FXML
    protected void btnTDMClickedGenTicket(){
        if(tdtikoutputpath.length() > 0){
            if(textTDMTitleID.getText().length() == 16 && textTDMEncryptedTitleKey.getText().length() == 32){
                if(textTDMTitleID.getText().toUpperCase().matches("[0-9A-F]+") && textTDMEncryptedTitleKey.getText().toUpperCase().matches("[0-9A-F]+")){

                    ObservableList<Ticket> item = FXCollections.observableArrayList();
                    Ticket tik = new Ticket();
                    tik.setTitleID(textTDMTitleID.getText().toUpperCase());
                    tik.setTitleKey(textTDMEncryptedTitleKey.getText().toUpperCase());
                    item.add(tik);

                    xml_handler.setTicketList(item);
                    item = xml_handler.readCommunityXMLFile(false);

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/TicketGenerator.fxml"));
                        Parent root2 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.getIcons().add(new Image("/resources/gciaicon.png"));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.DECORATED);
                        stage.setTitle("Generating Tickets");
                        stage.setScene(new Scene(root2));
                        stage.setResizable(false);
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                // consume event
                                event.consume();
                            }
                        });
                        TicketGeneratorController rb = fxmlLoader.getController();
                        rb.setInput(tdtikoutputpath, item);
                        stage.show();
                    }catch (Exception e){
                        StringWriter errors = new StringWriter();
                        e.printStackTrace(new PrintWriter(errors));
                        DebugLogger.log(errors.toString(), Level.SEVERE);
                    }
                }else{
                    Alert warning = new Alert(Alert.AlertType.ERROR);
                    Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    warning.setTitle("Error");
                    warning.setHeaderText("Illegal characters!");
                    warning.setContentText("The TitleID or TitleKey contains illeagal characters!");
                    warning.showAndWait();
                }
            }else{
                Alert warning = new Alert(Alert.AlertType.ERROR);
                Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/resources/gciaicon.png"));
                warning.setTitle("Error");
                warning.setHeaderText("Invalid TitleID or TitleKey");
                warning.setContentText("The TitleID or TitleKey is invalid!");
                warning.showAndWait();
            }
        }else{
            Alert warning = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Error");
            warning.setHeaderText("Outputfolder!");
            warning.setContentText("Please choose an .tik outputfolder first!");
            warning.showAndWait();
        }
    }

}
