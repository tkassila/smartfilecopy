package smartfilecopy;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javax.swing.event.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

class ListViewHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        //this method will be overrided in next step
    }
}

public class ConfigController {
    private File userConfig = null;
    private String strConfigSetName;
    private String strSourcePath;
    private String strTargetPath;
    private ConfigDirItem [] readItems = null;
    private ConfigDirItem mainCI = null;
    private ConfigDirItem selectedCI = null;
    private ConfigDirItem userSelectedCI = null;
//    private ConfigDirItem selectedItem = null;
    private boolean bTextFieldNameUnderRecursiveChange = false;
    private boolean bNewConfigDirSet = false;
    private DirectoryChooser directoryChooser;
    private Stage primaryStage;

    @FXML
    ListView<ConfigDirItem> listConfigs;
    @FXML
    TextField textFieldName;
    @FXML
    TextField textFieldSource;
    @FXML
    TextField textFieldTarget;
    @FXML
    Button buttonSelection;
    @FXML
    Button buttonSave;
    @FXML
    Button buttonDelete;
    @FXML
    Label labelMsg;
    @FXML
    Button buttonSouce;
    @FXML
    Button buttonTarget;
    @FXML
    Button buttonNewDirPairs;

    public ConfigController(File p_userConfig, ConfigDirItem [] p_readItems,
                            String p_strConfigSetName,
                            String p_strSourcePath, String p_strTargetPath)
    {
        readItems = p_readItems;
        userConfig = p_userConfig;
        strConfigSetName = p_strConfigSetName;
        strSourcePath = p_strSourcePath;
        strTargetPath = p_strTargetPath;
    }

    public void setPrimaryStage(Stage stage)
    {
        primaryStage = stage;
    }

    @FXML
    public void initialize() {
        if (strConfigSetName == null || strConfigSetName.trim().length() == 0)
            strConfigSetName = Controller.cnstDefaultPairName;
        bTextFieldNameUnderRecursiveChange = true;
        textFieldName.setText(strConfigSetName);
        textFieldSource.setText(strSourcePath);
        textFieldTarget.setText(strTargetPath);
        if (readItems != null && readItems.length > 0) {
            for(ConfigDirItem ci : readItems)
                listConfigs.getItems().add(ci);
        }
        if (readItems == null || readItems.length  == 0)
            loadConfigsFromFile(this.userConfig);
        // change same names when bNewConfigDirSet:
        if (bNewConfigDirSet)
        {
            int iCounter = 0;
            for (ConfigDirItem ci : listConfigs.getItems())
            {
                if (ci.getName().equals(mainCI.getName()))
                {
                    iCounter++;
                    ci.setName(ci.getName() +"" +iCounter);
                }
            }
        }

        if (strSourcePath != null && strConfigSetName != null && strTargetPath != null) {
            mainCI = new ConfigDirItem(strConfigSetName, strSourcePath, strTargetPath);
            userSelectedCI = mainCI;
            mainCI.setNewUnSaved(bNewConfigDirSet);
        }
        if (mainCI != null && this.listConfigs.getItems().size() == 0)
            listConfigs.getItems().add(mainCI);
        else
        {
            int iItem = 0;
            boolean founded = false;
            for(ConfigDirItem item : listConfigs.getItems())
            {
                if (item.getName().equals(mainCI.getName())) {
                    listConfigs.getItems().set(iItem, mainCI);
                    founded = true;
                    break;
                }
                iItem++;
            }
            if (!founded)
            {
                listConfigs.getItems().add(mainCI);
            }
            mainCI.setNewUnSaved(false);
            saveAllConfigDirItems();
        }

        bTextFieldNameUnderRecursiveChange = true;

        listConfigs.setOnMouseClicked(new ListViewHandler(){
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                ConfigDirItem selcted = (ConfigDirItem) listConfigs.getSelectionModel().getSelectedItem();
                if (selcted == null)
                    return;
                selectedCI = selcted;
                mainCI = selcted;
                bTextFieldNameUnderRecursiveChange = true;
                textFieldName.setText(selcted.getName());
                textFieldSource.setText(selcted.getSource());
                textFieldTarget.setText(selcted.getTarget());
                bTextFieldNameUnderRecursiveChange = false;
               // vboxMain.update();
            }
        });

        textFieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            if (bTextFieldNameUnderRecursiveChange)
                return;
            bTextFieldNameUnderRecursiveChange = true;
            if (existsAllReady(newValue)) {
                textFieldName.setText(oldValue);
                labelMsg.setText("'" + newValue + "' does exists allready. The old value is reset.");
            }
            else
            {
                String oldName = mainCI.getName();
                if (selectedCI == null)
                    selectedCI = mainCI;
                if (selectedCI == null)
                    return;

                selectedCI.setName(newValue);
                int iTem = 0;
                for(ConfigDirItem item : listConfigs.getItems())
                {
                    if (item.getName().equals(oldName)) {
                        listConfigs.getItems().set(iTem, selectedCI);
                        break;
                    }
                    iTem++;
                }
                listConfigs.refresh();
                /*
                int indSel = listConfigs.getSelectionModel().getSelectedIndex();
                if (indSel > -1)
                    listConfigs.getItems().set(indSel, mainCI);
                else
                {
                    int indItem = -1;
                    ConfigDirItem item = null;
                    ConfigDirItem foundeditem = null;

                    for(Object obj : listConfigs.getItems())
                    {
                        indItem++;
                        if (obj == null)
                            continue;
                        item = (ConfigDirItem) obj;
                        if (item.getName() == oldValue)
                        {
                            foundeditem = item;
                            break;
                        }
                    }
                    if (foundeditem != null)
                    {
                        listConfigs.getItems().set(indItem, mainCI);
                    }
                }
                 */
            }
            bTextFieldNameUnderRecursiveChange = false;
        });

        textFieldSource.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textFieldSource changed from " + oldValue + " to " + newValue);
            mainCI.setSource(newValue);
            /*
            if (selectedCI == null)
                selectedCI = mainCI;
             */
            if (mainCI == null)
                return;
            String name = mainCI.getName();
            int iTem = 0;
            for(ConfigDirItem item : listConfigs.getItems())
            {
                if (item.getName().equals(name)) {
                    listConfigs.getItems().set(iTem, mainCI);
                    break;
                }
                iTem++;
            }
            listConfigs.refresh();
        });

        textFieldTarget.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textFieldTarget changed from " + oldValue + " to " + newValue);
            mainCI.setTarget(newValue);
            String name = mainCI.getName();
            int iTem = 0;
            for(ConfigDirItem item : listConfigs.getItems())
            {
                if (item.getName().equals(name)) {
                    listConfigs.getItems().set(iTem, mainCI);
                    break;
                }
                iTem++;
            }
            listConfigs.refresh();
        });
    }

    @FXML
    protected void buttonNewDirPairsPressed()
    {
        bNewConfigDirSet = true;
        bTextFieldNameUnderRecursiveChange = true;
        textFieldName.setEditable(true);
        textFieldName.setText("");
        textFieldSource.setText("");
        textFieldTarget.setText("");
        mainCI = new ConfigDirItem("", "", "");
        mainCI.setNewUnSaved(true);
        bTextFieldNameUnderRecursiveChange = false;
        listConfigs.getItems().add(mainCI);
        listConfigs.refresh();
    }

    @FXML
    public void buttonSourcePressed()
    {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select source dir");
        File defaultDirectory = new File(Controller.getRootDir());
        if (textFieldSource.getText().trim().length()>0)
            defaultDirectory = new File(textFieldSource.getText());

        if (defaultDirectory.exists())
            directoryChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = directoryChooser.showDialog(this.primaryStage);
        if (selectedDirectory != null && selectedDirectory.exists())
        {
            textFieldSource.setText(selectedDirectory.getAbsolutePath());
            mainCI.setSource(selectedDirectory.getAbsolutePath());
            updateListingConfigWith(mainCI);
        }
    }

    @FXML
    public void buttonTargetPressed()
    {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select target dir");
        File defaultDirectory = new File(Controller.getRootDir());
        if (textFieldTarget.getText().trim().length()>0)
            defaultDirectory = new File(textFieldTarget.getText());

        if (defaultDirectory.exists())
            directoryChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = directoryChooser.showDialog(this.primaryStage);
        if (selectedDirectory != null && selectedDirectory.exists())
        {
            textFieldTarget.setText(selectedDirectory.getAbsolutePath());
            mainCI.setTarget(selectedDirectory.getAbsolutePath());
            updateListingConfigWith(mainCI);
        }
    }

    @FXML
    public void buttonDeleteClicked()
    {
        ConfigDirItem selected = listConfigs.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;
        boolean bSame = false;
        if (mainCI != null && selected != null && selected.getName().equals(mainCI.getName()))
            bSame = true;
        if (listConfigs.getItems().size() == 1) {
            labelMsg.setText("Cannot delete the last config item!");
            return;
        }
        else
            listConfigs.getItems().remove(selected);
        if (bSame)
        {
            if (listConfigs.getItems().size() == 0) {
                //textFieldName.setText(Controller.cnstDefaultPairName);
                bTextFieldNameUnderRecursiveChange = true;
                textFieldName.setText("");
                textFieldSource.setText("");
                textFieldTarget.setText("");
                bTextFieldNameUnderRecursiveChange = false;
                mainCI = null;
            }
            else
            {
                ConfigDirItem [] items = new ConfigDirItem[listConfigs.getItems().size()];
                items = listConfigs.getItems().toArray(items);
                mainCI = items[0];
                if (mainCI != null) {
                    bTextFieldNameUnderRecursiveChange = true;
                    textFieldName.setText(mainCI.getName());
                    textFieldSource.setText(mainCI.getSource());
                    textFieldTarget.setText(mainCI.getTarget());
                    bTextFieldNameUnderRecursiveChange = false;
                }
                else
                {
                    bTextFieldNameUnderRecursiveChange = true;
                    textFieldName.setText("");
                    textFieldSource.setText("");
                    textFieldTarget.setText("");
                    bTextFieldNameUnderRecursiveChange = false;
                }
            }
        }
        else
        {
            if (mainCI != null) {
                bTextFieldNameUnderRecursiveChange = true;
                textFieldName.setText(mainCI.getName());
                textFieldSource.setText(mainCI.getSource());
                textFieldTarget.setText(mainCI.getTarget());
                bTextFieldNameUnderRecursiveChange = false;
            }
        }
    }

    @FXML
    public void buttonSelectionClicked()
    {
        int indSel = listConfigs.getSelectionModel().getSelectedIndex();
        if (indSel == -1) {
            labelMsg.setText("Select an item in list control!");
            return;
        }
        mainCI = (ConfigDirItem)listConfigs.getSelectionModel().getSelectedItem();
        userSelectedCI = mainCI;
        labelMsg.setText("Selected an item to use later");
    }

    public ConfigDirItem getSelectedConfigDirItem()
    {
        return userSelectedCI;
    }

    public void setSeltectedConfigDirItem(ConfigDirItem item)
    {
        mainCI = item;
    }

    public void saveAllConfigDirItems()
    {
        writeJsonFile();
    }

    private boolean existsAllReady(String newValue)
    {
        boolean ret = false;
        if (newValue == null || newValue.trim().length() == 0)
            return false;
        ConfigDirItem [] items = new ConfigDirItem [listConfigs.getItems().size()];
        items = listConfigs.getItems().toArray(items);
        for (ConfigDirItem ci : items)
        {
            if (ci.getName().equals(newValue))
                return true;
        }
        return ret;
    }

    public void loadConfigsFromFile(File userConfig)
    {
        if (userConfig == null)
            return;
        if (!userConfig.exists())
            return;

        try {
                ConfigDirItem [] readItems = null;
                if ((readItems == null || readItems.length == 0) && listConfigs == null || listConfigs.getItems().size() == 0) {
                    readItems = ConfigDirItem.loadConfigsFromFile(userConfig);
                    listConfigs.getItems().addAll(readItems);
                }

                ConfigDirItem sameCiAsParameters = null;
                ConfigDirItem ci2;

                for (Object objci2: listConfigs.getItems())
                {
                    ci2 = (ConfigDirItem)objci2;
                    if (ci2.getSource().equals(textFieldSource.getText())
                    && ci2.getTarget().equals(textFieldTarget.getText()))
                        sameCiAsParameters = ci2;
                }
                if (sameCiAsParameters != null) {
                    mainCI = sameCiAsParameters;
                    bTextFieldNameUnderRecursiveChange = true;
                    textFieldName.setText(mainCI.getName());
                    bTextFieldNameUnderRecursiveChange = false;
                }
                else
                {
                    ;
                }
            } catch (Exception e) {
                e.printStackTrace();
                labelMsg.setText(e.getMessage());
            }   ;
    }

    private void writeJsonFile()
    {
        try {
            ConfigDirItem [] items = new ConfigDirItem [listConfigs.getItems().size()];
            items = listConfigs.getItems().toArray(items);
            ConfigDirItem.writeJsonFile(userConfig, items, mainCI);
            readItems = items;
        }catch (Exception ioe){
            ioe.printStackTrace();
            labelMsg.setText(ioe.getMessage());
        }
        /*
        JSONArray items = new JSONArray();
        JSONObject jsonObj ;
        ConfigDirItem ci;
        for(Object obj : listConfigs.getItems())
        {
            if (obj == null)
                continue;
            ci = (ConfigDirItem)obj;
            jsonObj = new JSONObject();
            jsonObj.put("name", ci.getName());
            jsonObj.put("source", ci.getSource());
            jsonObj.put("target", ci.getTarget());
            items.add(jsonObj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConfigDirItem.cnstConfigDirItems, items);
        try {
            FileWriter file = new FileWriter(userConfig);
            file.write(jsonObject.toJSONString());
            file.close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
         */
    }

    private void updateListingConfigWith(ConfigDirItem item)
    {
        int i = -1;
        for (ConfigDirItem c : listConfigs.getItems())
        {
            i++;
            if (c.getName().equals(item.getName())) {
                listConfigs.getItems().set(i, item);
                listConfigs.refresh();
                break;
            }
        }
    }

    public ConfigDirItem [] getReadItems()
    {
        ConfigDirItem [] ret = new ConfigDirItem[listConfigs.getItems().size()];
        ret = listConfigs.getItems().toArray(ret);
        return ret;
    }
}
