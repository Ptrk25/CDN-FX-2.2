package groovycia2;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TicketGeneratorController {

    @FXML
    public Label labelStatus;

    @FXML
    private ProgressBar progressBar1;

    private TicketGenerator tikgen;

    public void setInput(String path, ObservableList<Ticket> titlelist){
        tikgen = new TicketGenerator(path, titlelist);
        tikgen.setComponents(labelStatus, progressBar1);
        tikgen.start();
    }

    public void interrupt(){
        tikgen.interrupt();
    }
}
