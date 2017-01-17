package groovycia2;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.logging.Level;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        DebugLogger.init();

        /*
        Updater updater = new Updater();
        if(updater.checkForUpdates()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage stage2 = (Stage)alert.getDialogPane().getScene().getWindow();
            stage2.getIcons().add(new Image("/resources/gciaicon.png"));
            alert.setTitle("Update");
            alert.setHeaderText("New update found!");
            alert.setContentText("New update found! Do you want to update now?");

            Label label = new Label("Changelog:");

            TextArea textArea = new TextArea(updater.getChangelog());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            alert.getDialogPane().setExpandableContent(expContent);
            alert.getDialogPane().setExpanded(true);

            alert.initOwner(null);

            ButtonType Yes = new ButtonType("Yes");
            ButtonType No = new ButtonType("No");
            alert.getButtonTypes().setAll(Yes, No);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == Yes){
                if(updater.update()){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    Stage stage = (Stage)alert2.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    alert2.setTitle("Update");
                    alert2.setHeaderText("Update");
                    alert2.setContentText("Update successful! Please restart the program.");
                    alert2.initOwner(null);
                    alert2.showAndWait();
                    System.exit(0);
                }else{
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    Stage stage = (Stage)alert2.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/resources/gciaicon.png"));
                    alert2.setTitle("Update");
                    alert2.setHeaderText("Update");
                    alert2.setContentText("Update failed! Please contact Ptrk25.");
                    alert2.initOwner(null);
                    alert2.showAndWait();
                }
            }
        }*/

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/CDN-FX.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            scene.getStylesheets().add("gui/StyleSheet.css");
            primaryStage.setTitle("CDN-FX 2.2");
            primaryStage.getIcons().add(new Image("/resources/gciaicon.png"));
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(684);
            primaryStage.setMinWidth(1125);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                    event.consume();
                }
            });
            primaryStage.show();

            CDNFXController cdnfxController = loader.getController();
            cdnfxController.setCdnfxController(cdnfxController);

            Tools.unpackFiles();
        }catch (Exception e){
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            DebugLogger.log(errors.toString(), Level.SEVERE);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
