package groovycia2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable{

    @FXML
    private CheckBox chbxGenDebugmode,
            chbxTMSystemTitles,
            chbxTMNonUnique,
            chbxGenDisableXML,
            chbxGenNameForTID,
            chbxTMDefaultTicket,
            chbxTMDefaultOutput,
            chbxGenNoIndvFolders,
            chbxTDDefaultFile,
            chbxTDDefaultFolder,
            chbxTDDefaultTikFolder;

    @FXML
    private Button btnGenUpdateCommunityXML,
            btnOK,
            btnCancel,
            btnTMTicketSelect,
            btnTMOutputSelect,
            btnTDFile,
            btnTDFolder,
            btnTDTikFolder;

    @FXML
    private TextField textTMTicket,
            textTMOutput,
            textTDFile,
            textTDFolder,
            textTDTikFolder;

    private CDNFXController cdnfxController;

    //INIT
    public void initialize(URL location, ResourceBundle resources){
        initProperties();
    }

    public void setCdnfxController(CDNFXController cdnfxController){
        this.cdnfxController = cdnfxController;
    }

    private void initProperties(){
        //TICKETMANAGER
        if(PropertiesHandler.getTMInputPath() != null){
            if(PropertiesHandler.getTMInputPath().length() > 1){
                textTMTicket.setText(PropertiesHandler.getTMInputPath());
                chbxTMDefaultTicket.setSelected(true);
                btnTMTicketSelect.setDisable(false);
            }else{
                chbxTMDefaultTicket.setSelected(false);
                btnTMTicketSelect.setDisable(true);
            }
        }else{
            chbxTMDefaultTicket.setSelected(false);
            btnTMTicketSelect.setDisable(true);
        }

        if(PropertiesHandler.getTMOutputPath() != null){
            if(PropertiesHandler.getTMOutputPath().length() > 1){
                textTMOutput.setText(PropertiesHandler.getTMOutputPath());
                chbxTMDefaultOutput.setSelected(true);
                btnTMOutputSelect.setDisable(false);
            }else{
                chbxTMDefaultOutput.setSelected(false);
                btnTMOutputSelect.setDisable(true);
            }
        }else{
            chbxTMDefaultOutput.setSelected(false);
            btnTMOutputSelect.setDisable(true);
        }

        if(PropertiesHandler.getProperties("downloadsystemtitles") != null)
            chbxTMSystemTitles.setSelected(PropertiesHandler.getProperties("downloadsystemtitles").equals("yes"));
        if(PropertiesHandler.getProperties("downloadnonuniquetitles") != null)
            chbxTMNonUnique.setSelected(PropertiesHandler.getProperties("downloadnonuniquetitles").equals("yes"));
        if(PropertiesHandler.getProperties("debugmode") != null)
            chbxGenDebugmode.setSelected(PropertiesHandler.getProperties("debugmode").equals("yes"));
        if(PropertiesHandler.getProperties("titlename") != null)
            chbxGenNameForTID.setSelected(PropertiesHandler.getProperties("titlename").equals("yes"));
        if(PropertiesHandler.getProperties("disablexml") != null)
            chbxGenDisableXML.setSelected(PropertiesHandler.getProperties("disablexml").equals("yes"));
        if(PropertiesHandler.getProperties("noindvfolders") != null)
            chbxGenNoIndvFolders.setSelected(PropertiesHandler.getProperties("noindvfolders").equals("yes"));

        //TITLEDOWNLOADER
        if(PropertiesHandler.getTDAInputPath() != null ){
            if(PropertiesHandler.getTDAInputPath().length() > 1){
                textTDFile.setText(PropertiesHandler.getTDAInputPath());
                chbxTDDefaultFile.setSelected(true);
                btnTDFile.setDisable(false);
            }else{
                chbxTDDefaultFile.setSelected(false);
                btnTDFile.setDisable(true);
            }
        }else{
            chbxTDDefaultFile.setSelected(false);
            btnTDFile.setDisable(true);
        }

        if(PropertiesHandler.getTDOutputPath() != null){
            if(PropertiesHandler.getTDOutputPath().length() > 1){
                textTDFolder.setText(PropertiesHandler.getTDOutputPath());
                chbxTDDefaultFolder.setSelected(true);
                btnTDFolder.setDisable(false);
            }else{
                chbxTDDefaultFolder.setSelected(false);
                btnTDFolder.setDisable(true);
            }
        }else{
            chbxTDDefaultFolder.setSelected(false);
            btnTDFolder.setDisable(true);
        }

        if(PropertiesHandler.getTDTikPath() != null){
            if(PropertiesHandler.getTDTikPath().length() > 1){
                textTDTikFolder.setText(PropertiesHandler.getTDTikPath());
                chbxTDDefaultTikFolder.setSelected(true);
                btnTDTikFolder.setDisable(false);
            }else{
                chbxTDDefaultTikFolder.setSelected(false);
                btnTDTikFolder.setDisable(true);
            }
        }else{
            chbxTDDefaultTikFolder.setSelected(false);
            btnTDTikFolder.setDisable(true);
        }

    }

    @FXML
    protected void ticketTMSelected(){
        if(chbxTMDefaultTicket.isSelected()){
            btnTMTicketSelect.setDisable(false);
        }else{
            btnTMTicketSelect.setDisable(true);
            PropertiesHandler.setProperties("", "tmticketdb");
        }
    }

    @FXML
    protected void outputTMSelected(){
        if(chbxTMDefaultOutput.isSelected()){
            btnTMOutputSelect.setDisable(false);
        }else{
            btnTMOutputSelect.setDisable(true);
            PropertiesHandler.setProperties("", "tmoutputfolder");
        }
    }

    //FUNCTIONS
    @FXML
    protected void btnClickedCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void btnClickedOK() throws Exception{
        PropertiesHandler.saveProperties();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        if(PropertiesHandler.getProperties("debugmode") != null)
            if(PropertiesHandler.getProperties("debugmode").equals("no"))
                if(chbxGenDebugmode.isSelected())
                    DebugLogger.init();
        cdnfxController.initProperties();
        stage.close();
    }

    @FXML
    protected void btnGenClickedUpdateCommunityXML() {
        XMLUpdater xmlu = new XMLUpdater();
        Boolean need = xmlu.checkForUpdates();
        if(need){
            xmlu.update();
            Alert warning = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Information");
            warning.setHeaderText("Community XML Update");
            warning.setContentText("Update successful!");
            warning.showAndWait();
        }else{
            Alert warning = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Information");
            warning.setHeaderText("Community XML Update");
            warning.setContentText("No update found!");
            warning.showAndWait();
        }
    }

    @FXML
    protected void btnTMClickedOutputSelect() throws Exception{
        String path2 = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path2 = path2.substring(1, path2.lastIndexOf("/")) + "/";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(path2));
        File selectedDirectory = directoryChooser.showDialog(btnCancel.getScene().getWindow());
        if(selectedDirectory != null){
            String path = selectedDirectory.getPath();
            textTMOutput.setText(path);
            path = path.replaceAll(":", "!");
            PropertiesHandler.setProperties(path, "tmoutputfolder");
        }
    }

    @FXML
    protected void btnTMClickedTicketSelect() throws Exception{
        String path2 = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path2 = path2.substring(1, path2.lastIndexOf("/")) + "/";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open ticket.db");
        fileChooser.setInitialDirectory(new File(path2));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ticket.db", "*.db"));
        File selectedFile = fileChooser.showOpenDialog(btnCancel.getScene().getWindow());

        if(selectedFile != null){
            String path = selectedFile.getPath();
            textTMTicket.setText(path);
            path = path.replaceAll(":", "!");
            PropertiesHandler.setProperties(path, "tmticketdb");
        }
    }

    @FXML
    protected void chbxGenClickedDebugmode() {
        if(chbxGenDebugmode.isSelected()){
            Alert warning = new Alert(Alert.AlertType.INFORMATION);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Information");
            warning.setHeaderText("Enable Debugmode");
            warning.setContentText("If you are having a problem with this program, enable this option then replicate the issue. A log file will be produced by the program. Report the issue by posting the log file in the GroovyCIA thread");
            warning.showAndWait();
            PropertiesHandler.setProperties("yes", "debugmode");
        }else{
            PropertiesHandler.setProperties("no", "debugmode");
        }
    }

    @FXML
    protected void chbxTMClickedNonUnique() {
        if(chbxTMNonUnique.isSelected()){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Warning");
            warning.setHeaderText("Download Non-Unique Titles");
            warning.setContentText("This will allow you to download content from your Ticket.db that is not attached to your eShop.\n\n" +
                    "Only select this option if you have a PREINSTALLED/LEGIT CIA in your Ticket.db.\n\n" +
                    "Otherwise, content may not install or work properly as intended.\n\n" +
                    "USE IT AT YOUR RISK!!");
            warning.showAndWait();
            PropertiesHandler.setProperties("yes", "downloadnonuniquetitles");
        }else{
            PropertiesHandler.setProperties("no", "downloadnonuniquetitles");
        }
    }

    @FXML
    protected void chbxTMClickedSystemTitles() {
        if(chbxTMSystemTitles.isSelected()){
            Alert warning = new Alert(Alert.AlertType.WARNING);
            Stage stage = (Stage)warning.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/resources/gciaicon.png"));
            warning.setTitle("Warning");
            warning.setHeaderText("Download System Titles");
            warning.setContentText("This feature is experimental and should only be used for debugging purposes.\n\n" +
                    "Do not use this option as a NUS replacement.\n" +
                    "USE IT AT YOUR OWN RISK!!");
            warning.showAndWait();
            PropertiesHandler.setProperties("yes", "downloadsystemtitles");
        }else{
            PropertiesHandler.setProperties("no", "downloadsystemtitles");
        }
    }

    @FXML
    protected void chbxGenClickedDisableXML(){
        if(chbxGenDisableXML.isSelected())
            PropertiesHandler.setProperties("yes", "disablexml");
        else
            PropertiesHandler.setProperties("no", "disablexml");
    }

    @FXML
    protected void chbxGenClickedNameForTID(){
        if(chbxGenNameForTID.isSelected())
            PropertiesHandler.setProperties("yes", "titlename");
        else
            PropertiesHandler.setProperties("no", "titlename");
    }

    @FXML
    protected void chbxGenClickedNoIndvFolders(){
        if(chbxGenNoIndvFolders.isSelected())
            PropertiesHandler.setProperties("yes", "noindvfolders");
        else
            PropertiesHandler.setProperties("no", "noindvfolders");
    }

    @FXML
    protected void chbxTDClickedDefaultFile(){
        if(chbxTDDefaultFile.isSelected()){
            btnTDFile.setDisable(false);
        }else{
            btnTDFile.setDisable(true);
            PropertiesHandler.setProperties("", "tdfile");
        }
    }

    @FXML
    protected void chbxTDClickedDefaultFolder(){
        if(chbxTDDefaultFolder.isSelected()){
            btnTDFolder.setDisable(false);
        }else{
            btnTDFolder.setDisable(true);
            PropertiesHandler.setProperties("", "tdfolder");
        }
    }

    @FXML
    protected void chbxTDClickedDefaultTikFolder(){
        if(chbxTDDefaultTikFolder.isSelected()){
            btnTDTikFolder.setDisable(false);
        }else{
            btnTDTikFolder.setDisable(true);
            PropertiesHandler.setProperties("", "tdtikfolder");
        }
    }

    @FXML
    protected void btnTDClickedFile() throws Exception{
        String path2 = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path2 = path2.substring(1, path2.lastIndexOf("/")) + "/";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open encTitleKeys.bin");
        fileChooser.setInitialDirectory(new File(path2));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("encTitleKeys", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(btnCancel.getScene().getWindow());

        if(selectedFile != null){
            String path = selectedFile.getPath();
            textTDFile.setText(path);
            path = path.replaceAll(":", "!");
            PropertiesHandler.setProperties(path, "tdfile");
        }
    }

    @FXML
    protected void  btnTDClickedFolder() throws Exception{
        String path2 = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path2 = path2.substring(1, path2.lastIndexOf("/")) + "/";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(path2));
        File selectedDirectory = directoryChooser.showDialog(btnCancel.getScene().getWindow());
        if(selectedDirectory != null){
            String path = selectedDirectory.getPath();
            textTDFolder.setText(path);
            path = path.replaceAll(":", "!");
            PropertiesHandler.setProperties(path, "tdfolder");
        }
    }

    @FXML
    protected void btnTDClickedTikFolder() throws Exception{
        String path2 = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        path2 = path2.substring(1, path2.lastIndexOf("/")) + "/";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(path2));
        File selectedDirectory = directoryChooser.showDialog(btnCancel.getScene().getWindow());
        if(selectedDirectory != null){
            String path = selectedDirectory.getPath();
            textTDTikFolder.setText(path);
            path = path.replaceAll(":", "!");
            PropertiesHandler.setProperties(path, "tdtikfolder");
        }
    }
}
