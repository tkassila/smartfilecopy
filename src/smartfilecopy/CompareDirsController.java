package smartfilecopy;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class CompareDirsController {


    @FXML
    ListView<ConfigDirItem> listConfigs;
    @FXML
    Button buttonClose;
    @FXML
    Button buttonDelete;
    @FXML
    Button buttonSeltectDir;
    @FXML
    Button buttonAdd;
    @FXML
    Label labelMsg;

    public CompareDirsController()
    {
    }

    @FXML
    public void initialize() {
        labelMsg.setStyle("-fx-font-weight: bold");
    }

    @FXML
    protected void buttonCloseClicked()
    {
    }

    @FXML
    protected void buttonAddClicked()
    {

    }

    @FXML
    protected void buttonSeltectDirClicked()
    {

    }

    @FXML
    protected void buttonDeleteClicked()
    {

    }

}
