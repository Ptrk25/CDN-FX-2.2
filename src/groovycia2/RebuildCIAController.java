package groovycia2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class RebuildCIAController {

    @FXML
    public Label labelStatus;

    @FXML
    private ProgressBar progressBar1;

    public void setInput(String path){
        Downloader dl = new Downloader(null, path, true);
        dl.setComponents(labelStatus, null, null, null, null, null, null, null, progressBar1, null, null);
        dl.setDownload(false);
        dl.start();
    }

}
