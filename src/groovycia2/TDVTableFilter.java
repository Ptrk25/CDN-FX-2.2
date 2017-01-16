package groovycia2;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class TDVTableFilter {

    private static String text = "";
    private static String categoryText = "";
    private static boolean isPrepared = false;

    public static FilteredList<Ticket> filteredTickets;

    public static SortedList<Ticket> createTableFilter(TextField textSearch, ListView listView){

        if(isPrepared)
            return new SortedList<>(filteredTickets);

        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                newValue = "";
            text = newValue.toLowerCase();
            filterTickets();
        });

        //Listview
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                categoryText = newValue.split(" ")[0];
                if(categoryText.equals("DownloadPlayChild"))
                    categoryText = "DLP";
                filterTickets();
            }
        });

        isPrepared = true;
        return new SortedList<>(filteredTickets);

    }

    private static void filterTickets(){
        filteredTickets.setPredicate(ticket -> false);
        filteredTickets.setPredicate(ticket -> {
            if(categoryText.equals("All"))
                categoryText = "";
            if(categoryText.equals("DSiSystemApp"))
                categoryText = "DSiSysApp";
            if(categoryText.equals("DSiSystemData"))
                categoryText = "DSiSysDat";

            //TEXTSEARCH FILTER
            if(text.length() != 0){
                String name = ticket.getName();
                String tid = ticket.getTitleID();
                String region = ticket.getRegion();
                String serial = ticket.getSerial();
                String type = ticket.getType();

                if(name == null)
                    name = "";
                if(region == null)
                    region = "";
                if(serial == null)
                    serial = "";

                name = name.toLowerCase();
                region = region.toLowerCase();
                serial = serial.toLowerCase();
                type = type.toLowerCase();

                if((name.contains(text) || tid.contains(text) || region.contains(text) || serial.contains(text) || type.contains(text))){
                    if(categoryText.length() > 0){
                        if(ticket.getType().equals(categoryText))
                            return true;
                        else
                            return false;
                    }else
                        return true;
                }
            }else{
                if(categoryText.length() > 0){
                    if(ticket.getType().equals(categoryText))
                        return true;
                    else
                        return false;
                }else
                    return true;
            }
            return false;

        });
    }

}
