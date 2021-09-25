package smartfilecopy;

import java.io.*;
import java.util.Properties;
import java.util.Comparator;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import java.util.Arrays;
// import java.util.Collections;

////import org.xml.sax.SAXException;
//import org.xml.sax.ContentHandler;
import java.awt.Desktop;
import java.util.function.UnaryOperator;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Screen;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

// import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.awt.image.BufferedImage;

//import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
//import org.apache.pdfbox.pdmodel.common.PDMetadata;

/*
import org.apache.tika.sax.WriteOutContentHandler;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
//import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
*/

/*
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
 */
// import de.schlichtherle.truezip.file.TFile;
// import de.schlichtherle.truezip.fs.FsPath;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.IOUtils;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.beans.value.ObservableValue;
import javax.imageio.ImageIO;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.concurrent.Task;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.Node;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.*;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

class FoundedHashFiles {
    public String title;
    public File [] arrFiles;
}

public class Controller {

    private DirectoryChooser directoryChooser;
    private StringBuffer sbError = null;

    @FXML
    TextField textFieldSourcePath;
    @FXML
    Button buttonSource;
    @FXML
    TextField textFieldTargetPath;
    @FXML
    Button buttonTarget;
    @FXML
    ListView<ListViewDirItem> listViewDirs;
    @FXML
    HBox hBoxListDirs;
    @FXML
    TextField textFieldCurrentDir;
    @FXML
    VBox vboxMain;
    @FXML
    Button buttonExecute;
    @FXML
    Label labelStartTime;
    @FXML
    Label labelEndTime;
    @FXML
    Button buttonShowErrors;
    @FXML
    Label laberMessage;
    @FXML
    Button buttonDirPairs;
    @FXML
    TextField textFieldDirPairs;
    @FXML
    RadioButton radioButtonZip;
    @FXML
    RadioButton radioButton7z;
    @FXML
    CheckBox checkBoxCompress;
    @FXML
    RadioButton radioButtonTar;
    @FXML
    RadioButton radioButtonCopyTitles;
    @FXML
    Button buttonCompareDirs;
    @FXML
    RadioButton radioButtonFindTuples;
    @FXML
    RadioButton radioButtonFindRealTuples;
    @FXML
    RadioButton radioButtonCopyFlles;
    @FXML
    TextField textFieldCompareDirs;
    @FXML
    CheckBox checkBoxTitles;
    @FXML
    RadioButton radioButtonSortBaseFNames;
    @FXML
    Label labelList;
    @FXML
    RadioButton radioButtonSortSimilarBaseFNames;
    @FXML
    RadioButton radioButtonMissigTitles;
    @FXML
    RadioButton radioButtonSelectCopyingTitles;
    @FXML
    CheckBox checkBoxSimilar;
    @FXML
    TextField textFiledSimilarity;
    @FXML
    CheckBox checkBoxStartWith;
    @FXML
    CheckBox checkBoxOnlyEpubs;
    @FXML
    RadioButton radioButtonSortTypeOf;
    @FXML
    ChoiceBox<String> choiceboxType;
    @FXML
    RadioButton radioButtonSource;
    @FXML
    RadioButton radioButtonTarget;
/*    @FXML
    HBox hBoxLIst;
*/

    private Stage primaryStage;
    private File [] arrTargetChildren;
  //  private ConfigDirItem cdiritem = null;
    private ConfigController configController = null;
    private CompareDirsController compareDirsController;
    public static final String cnstDefaultPairName = "Unnamed config set";
    private File useConfigFile = null;
    private ConfigDirItem [] readItems = null;
    private ConfigDirItem mainCI = null;
    private File userAppDir = null;
    private boolean bTextFieldNameUnderRecursiveChange = false;
    private boolean radioButtonUnderRecursiveChange = false;
    private boolean radioButtonUnderRecursiveChange2 = false;
    private boolean bCompress = false;
    private String selectedCompres = null;
    private ArchiveOutputStream compressOStream = null;
    private File [] arrCompareDirs = null;
    private ExtensionsFilter ebookfilter = new ExtensionsFilter(new String[] {".epub", ".pdf",".mobi"});
    private ExtensionsFilter ebookfilterAll = new ExtensionsFilter(new String[] {".epub", ".pdf",".mobi",".zip"});
    private Properties proerties = null;

    private HashMap<String,FoundedHashFiles> hmSourceBooks = new HashMap<String,FoundedHashFiles>();
    private HashMap<String,FoundedHashFiles> hmTargetBooks = new HashMap<String,FoundedHashFiles>();
    private HashMap<String,FoundedHashFiles> hmCompareBooks = new HashMap<String,FoundedHashFiles>();
    private boolean  bDeleteFile = false;
    private Task<Integer> task = null;
    private int iStrikeChars = 0;
    private boolean bTaskExecuting = false;
    private boolean bDebug = false;
    private List<File> fileList = null;
    private boolean bSimilarEbooksFounded = false;
    private String strMessae = "";
    private ObservableList<ListViewDirItem> listitems = null;
    private JaroWinklerSimilarity simalarity = new JaroWinklerSimilarity();
    private double dSimilar = 0.9;
    private StartWithimportHashMap hashMap = null;
    private List<File> dirsMissingTitle = null;
    private ChangeListener<Boolean> sourcetargetlistener = null;
    private String strSourceTarget = new String("");

    final String cnst_prop_file = "smaartcopy.properties";
    final String cnst_none_empthy_dirs = "none_empthy_dirs";
    final String cnst_ebooktypes = "ebooks_extensions";

    public VBox getVboxMain() { return vboxMain; }
    public HBox getHBoxListDirs() { return hBoxListDirs; }
    public void setPrimaryStage(Stage stage)
    {
        primaryStage = stage;
    }

    @FXML
    public void initialize() {

        proerties = getProperties();
        ebookfilter = getEbooksFilter();
        ebookfilterAll = getAllEbooksFilter();

        radioButtonSource.setSelected(true);

        choiceboxType.getItems().add("-- all");
        for(String f : getAddPointCharArray(proerties.get(cnst_none_empthy_dirs).toString().split(",")))
            choiceboxType.getItems().add(f.trim());
        choiceboxType.getSelectionModel().select(0);
        choiceboxType.setDisable(true);

        sourcetargetlistener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (radioButtonCopyTitles.isSelected() || radioButtonSelectCopyingTitles.isSelected()) {
                    radioButtonTarget.setDisable(true);
                    radioButtonSource.setDisable(true);
                } else {
                    if (isNowSelected) {
                        radioButtonTarget.setDisable(false);
                        radioButtonSource.setDisable(false);
                    }
                }
            }
        };

        radioButtonSortTypeOf.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    choiceboxType.setDisable(false);
                } else {
                    choiceboxType.setDisable(true);
                }
            }
        });

        radioButtonCopyTitles.selectedProperty().addListener(sourcetargetlistener);
        radioButtonSelectCopyingTitles.selectedProperty().addListener(sourcetargetlistener);
        radioButtonFindTuples.selectedProperty().addListener(sourcetargetlistener);
        radioButtonFindRealTuples.selectedProperty().addListener(sourcetargetlistener);
        radioButtonSortBaseFNames.selectedProperty().addListener(sourcetargetlistener);
        radioButtonSortSimilarBaseFNames.selectedProperty().addListener(sourcetargetlistener);

        String tooltiptext = "This radio button enables right side of radio buttons before the execution button";
        Tooltip tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        checkBoxTitles.setTooltip(tip);
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        checkBoxCompress.setTooltip(tip);
        checkBoxStartWith.setDisable(true);

        tooltiptext = "This checkbox button when is on and a copy radiobutton is also on, will copy only epub books from a source dir into tagert dir";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        checkBoxOnlyEpubs.setTooltip(tip);
        checkBoxOnlyEpubs.setDisable(true);

        checkBoxSimilar.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // checkBoxSimilar.setSelected(!newValue);
                textFiledSimilarity.setDisable(!newValue);
            }
        });

        radioButtonCopyTitles.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    radioButtonSource.setDisable(true);
                    radioButtonTarget.setDisable(true);
                }
            }
        });

        StringConverter<Double> converter = new StringConverter<Double>() {

            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0;
                } else {
                    return Double.valueOf(s);
                }
            }


            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };

        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, dSimilar, filter);
        TextField textField = new TextField();
        textFiledSimilarity.setTextFormatter(textFormatter);
        textFiledSimilarity.setText("" +dSimilar);
        textFiledSimilarity.setDisable(true);

        tooltiptext = "This radio button selects a source dir and below and files copy into a target dir";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonCopyFlles.setTooltip(tip);

        // radioButtonCopyFlles.setSelected(true);
        laberMessage.setText("Check either Titles or Make Compress checkbox and after it, some radiobutton before pressing the Executte button.");

        tooltiptext = "This radio button will find tuple title book files below into a target dir";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonFindTuples.setTooltip(tip);
        radioButtonFindTuples.setDisable(true);

        tooltiptext = "This radio button will find tuple title book files in the same format below into a target dir";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonFindRealTuples.setTooltip(tip);
        radioButtonFindRealTuples.setDisable(true);

        radioButton7z.setDisable(true);
        radioButtonCopyFlles.setDisable(true);

        tooltiptext = "This radiobutton will copy title ebook files from source dir below target dir";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonCopyTitles.setTooltip(tip);

        tooltiptext = "This radiobutton will copy selected title ebook from source dir below target dir";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonSelectCopyingTitles.setTooltip(tip);
        tooltiptext = "This checkbox will find similar pdf file names";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        checkBoxSimilar.setTooltip(tip);

        tooltiptext = "Select some of radio buttons before pressing this button";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        buttonExecute.setTooltip(tip);

        tooltiptext = "Sort founded ebooks after file base name below a source directory";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonSortBaseFNames.setTooltip(tip);

        tooltiptext = "Seek empty doc dirs which do not contain books/documents/.zips (config var: none_empthy_dirs)";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonMissigTitles.setTooltip(tip);

        tooltiptext = "Sort founded ebooks after file base name from a source directory";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonSortBaseFNames.setTooltip(tip);

        checkBoxSimilar.setDisable(true);
        radioButtonSelectCopyingTitles.setDisable(true);
        radioButtonCopyTitles.setDisable(true);
        radioButtonTar.setDisable(true);

        checkBoxTitles.setSelected(false);
        checkBoxTitles.setSelected(false);
        buttonShowErrors.setDisable(true);
        textFieldDirPairs.setEditable(false);
        laberMessage.setStyle("-fx-font-weight: bold");
        labelList.setStyle("-fx-font-weight: bold");
        sbError = new StringBuffer();
        // sbError.append("error");
        textFieldDirPairs.setText(cnstDefaultPairName);
        String envUser = (File.separatorChar == '\\' ? "USERPROFILE" : "HOME");
        String useHome = System.getenv(envUser);
        userAppDir = new File(useHome +File.separator +".smartfilecopy");
        if (!userAppDir.exists())
            userAppDir.mkdir();
        useConfigFile = new File(userAppDir.getAbsolutePath() +File.separator +"config.json");
        try {
            if (useConfigFile.exists()) {
                readItems = ConfigDirItem.loadConfigsFromFile(useConfigFile);
                if (mainCI == null && ConfigDirItem.selectedtem != null)
                    mainCI = ConfigDirItem.selectedtem;
                if (mainCI == null && readItems.length > 0)
                    mainCI = readItems[0];
            }
        }catch (Exception e){
            e.printStackTrace();
            laberMessage.setText(e.getMessage());
            return;
        }
        if (mainCI != null)
        {
           bTextFieldNameUnderRecursiveChange = true;
           textFieldDirPairs.setText(mainCI.getName());
           textFieldSourcePath.setText(mainCI.getSource());
           textFieldTargetPath.setText(mainCI.getTarget());
           bTextFieldNameUnderRecursiveChange = false;
        }
        else
        {
            bTextFieldNameUnderRecursiveChange = true;
            textFieldDirPairs.setText("");
            textFieldSourcePath.setText("");
            textFieldTargetPath.setText("");
            bTextFieldNameUnderRecursiveChange = false;
        }

        radioButtonZip.setDisable(true);
        radioButtonZip.setSelected(false);
        radioButtonSortBaseFNames.setSelected(false);

        tooltiptext = "Sort founded ebooks after file base name from a source directory";
        tip = new Tooltip(tooltiptext);
        tip.setStyle("-fx-font-weight: bold");
        tip.setStyle("-fx-font-size: 14");
        radioButtonSortSimilarBaseFNames.setTooltip(tip);

        radioButtonSortSimilarBaseFNames.setSelected(false);
        radioButtonMissigTitles.setSelected(false);
        radioButtonSortTypeOf.setSelected(false);
        radioButtonSortTypeOf.setDisable(true);
        radioButtonSource.setDisable(true);
        radioButtonTarget.setDisable(true);
        radioButtonSortSimilarBaseFNames.setDisable(true);
        radioButtonMissigTitles.setDisable(true);
        radioButtonSortBaseFNames.setDisable(true);
        //radioButtonZip.managedProperty().bind(myHBox.visibleProperty());
        // radioButtonTar.setVisible(false);
        radioButtonZip.setUserData("zip");
        radioButton7z.setDisable(true);
        radioButtonTar.setDisable(true);
        radioButton7z.setUserData("tar.gz");
        radioButtonTar.setUserData("tar");

        buttonCompareDirs.setDisable(true);
       // HBox.setHgrow(hBoxLIst, Priority.ALWAYS);
        //hBoxLIst.fillHeightProperty(vboxMain.heightProperty());
        // vboxMain.fillWidthProperty().bind(hBoxLIst.widthProperty());
        // hBoxLIst.setStyle("-fx-background-color: #" + "black");

        listViewDirs.getStylesheets().add(this.getClass().getResource("scrollbarstyle.css").toExternalForm());

        listitems = FXCollections.observableArrayList();
        listViewDirs.setItems(listitems);

        /* this bwlow code will make listview show items bably when removing!
        Correspondind bold rows etc has done with css:
        listViewDirs.setCellFactory(cell -> new ListCell<ListViewDirItem>(){
            @Override
            protected void updateItem(ListViewDirItem item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || item == null
                        || (item != null && (item.title == null || item.title.trim().length() == 0)))
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setTooltip(null);
                        }});
                else
                // First, we are only going to update the cell if there is actually an item to display there.
                if (!empty && item != null) {

                    // Set the text of the cell to the Person's name
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setText(item.showname);
                        }});
                    if (item.path == null)
                    {
                        // empty row in the list
                        // System.err.println("item.path is null!");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setTooltip(null);
                                setStyle(null);
                                setText(null);
                            }});
                        return;
                    }
                    File f = new File(item.path);
                    if (f.exists()) {
                        String tooltopTitle = item.title;
                        if (tooltopTitle != null) {
                            Tooltip tooltip = new Tooltip();
                            tooltip.setText(tooltopTitle);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    setTooltip(tooltip);
                                }});
                        }
                    }
                    else
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setTooltip(null);
                            }});

                    // If the Person has the "important" flag, we can make the text bold here
                    if (!empty) {
                        setStyle("-fx-font-weight: bold");
                    } else {
                        // Remove any styles from the cell, because this Person isn't important
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setTooltip(null);
                            }});
                    }
                } else {
                    // If there is no item to display in this cell, set the text to null
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            setTooltip(null);
                        }});

                }
            }
        });
         */

        listViewDirs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if( mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    System.out.println("isSecondaryButtonDown");
                    mouseEvent.consume();
                    int indSelection = listViewDirs.getSelectionModel().getSelectedIndex();
                    if (indSelection == -1)
                        return;
                    ListViewDirItem item = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
                    if (item == null || item.path == null)
                        return;

                    File f = new File(item.path);
                    if (!f.exists())
                        return;

                    if (!f.isDirectory() && !isDocument(f))
                        return;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Desktop.getDesktop().open(f);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else
               if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("Double clicked");
                        mousePressedInListViewDClick(mouseEvent);
                    }
                    else
                    if(mouseEvent.getClickCount() == 1){
                        System.out.println("1 clicked");
                        mousePressedInListView1Click(mouseEvent);
                    }
               }
            }
        });

        // listViewDirs.setOnMouseClicked(e ->
        // listViewDirs.setOnMouseReleased(MouseEvent.MOUSE_PRESSED, e ->
        /*
        listViewDirs.addEventFilter(MouseEvent.MOUSE_EXITED, e ->
        {
            // if an user will see document as open::
            if( e.isSecondaryButtonDown()) {
                e.consume();
                int indSelection = listViewDirs.getSelectionModel().getSelectedIndex();
                if (indSelection == -1)
                    return;
                ListViewDirItem item = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
                if (item == null || item.path == null)
                    return;

                File f = new File(item.path);
                if (!f.exists())
                    return;

                if (!isDocument(f))
                    return;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Desktop.getDesktop().open(f);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else
            {
                    mousePressedInListView(e);
            }
        });
         */

        /*
        listViewDirs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // when an user has pressed double click with mouse:
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 ?* &&
                        (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0) *?) {
                    ListViewDirItem objSelected = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
                    if (objSelected == null)
                        return;
                    if (!radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                        && !radioButtonSortSimilarBaseFNames.isSelected() && !radioButtonMissigTitles.isSelected())
                        return; // do nothing

                    String willDeleteFilePath = (String)objSelected.path;
                    if (willDeleteFilePath != null && willDeleteFilePath.trim().length() != 0) {
                        if (radioButtonSelectCopyingTitles.isSelected()) {
                            File f = new File(willDeleteFilePath);
                            File targetDir = new File(textFieldTargetPath.getText());
                            try {
                                writeBookFilesIntoTarget(targetDir, f);
                                int ind = listViewDirs.getSelectionModel().getSelectedIndex();
                                if (ind > -1)
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            listitems.remove(ind);
                                            laberMessage.setText("Copyed book: " +f.getAbsolutePath() +" into target dir");
                                        }});

                                // dddd
                            }catch (IOException ioe){
                                sbError.append("cannot write " +f.getAbsolutePath() +" into " +targetDir.getAbsolutePath() +"\n");
                            }
                        }
                        else
                        if(radioButtonFindTuples.isSelected())
                            deleletTitleFile(willDeleteFilePath);
                    }
                    //your code here
                }
                else
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 ?* &&
                        (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0) *?) {
                    ListViewDirItem objSelected = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
                    if (objSelected == null)
                        return;
                    if (!radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                            && !radioButtonSortSimilarBaseFNames.isSelected() && !radioButtonMissigTitles.isSelected())
                        return; // do nothing

                    String willDeleteFilePath = (String)objSelected.path;
                    if (willDeleteFilePath != null && willDeleteFilePath.trim().length() != 0) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                textFieldCurrentDir.setText(willDeleteFilePath);
                            }});
                    }
                }}
        });
        */

        checkBoxCompress.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (radioButtonUnderRecursiveChange)
                    return;
                radioButtonUnderRecursiveChange = true;
                if (newValue) {
                    checkBoxTitles.setSelected(false);
                    checkBoxTitles.setDisable(true);
                }
                else
                {
                    checkBoxTitles.setDisable(false);
                    radioButtonZip.setSelected(false);
                    radioButton7z.setSelected(false);
                    radioButtonTar.setSelected(false);
                }
                radioButtonZip.setDisable(!newValue);
                radioButton7z.setDisable(!newValue);
                radioButtonTar.setDisable(!newValue);
                bCompress = newValue;
                selectedCompres = null;
                radioButtonUnderRecursiveChange = false;
            }
        });

        checkBoxTitles.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (radioButtonUnderRecursiveChange2)
                    return;
                radioButtonUnderRecursiveChange2 = true;
                if (newValue) {
                    checkBoxCompress.setSelected(false);
                    checkBoxCompress.setDisable(true);
                }
                else
                {
                    checkBoxCompress.setDisable(false);
                    radioButtonCopyTitles.setSelected(false);
                    radioButtonSelectCopyingTitles.setSelected(false);
                    radioButtonSortTypeOf.setSelected(false);
                    radioButtonSource.setSelected(false);
                    radioButtonTarget.setSelected(false);
                    checkBoxSimilar.setSelected(false);
                    checkBoxStartWith.setSelected(false);
                    checkBoxOnlyEpubs.setSelected(false);
                    radioButtonFindTuples.setSelected(false);
                    radioButtonFindRealTuples.setSelected(false);
                    radioButtonCopyFlles.setSelected(false);
                    radioButtonSortBaseFNames.setSelected(false);
                    radioButtonSortSimilarBaseFNames.setSelected(false);
                    radioButtonMissigTitles.setSelected(false);
                    radioButtonSortTypeOf.setSelected(false);
                }

                /*
                radioButtonZip.setDisable(newValue);
                radioButton7z.setDisable(newValue);
                radioButtonTar.setDisable(newValue);
                buttonCompareDirs.setDisable(newValue ? true : !radioButtonCopyTitles.isSelected());
                 */
                radioButtonSelectCopyingTitles.setDisable(!newValue);
                radioButtonSortTypeOf.setDisable(!newValue);
                radioButtonSource.setDisable(!newValue);
                radioButtonTarget.setDisable(!newValue);
                checkBoxSimilar.setDisable(!newValue);
                checkBoxStartWith.setDisable(!newValue);
                checkBoxOnlyEpubs.setDisable(!newValue);
                radioButtonCopyTitles.setDisable(!newValue);
                radioButtonFindTuples.setDisable(!newValue);
                radioButtonFindRealTuples.setDisable(!newValue);
                radioButtonCopyFlles.setDisable(!newValue);
                radioButtonSortBaseFNames.setDisable(!newValue);
                radioButtonSortSimilarBaseFNames.setDisable(!newValue);
                radioButtonMissigTitles.setDisable(!newValue);
                radioButtonSortTypeOf.setDisable(!newValue);
                radioButtonSource.setDisable(!newValue);
                radioButtonTarget.setDisable(!newValue);
                radioButtonUnderRecursiveChange2 = false;
            }
        });

        ToggleGroup rgroupp = radioButtonZip.getToggleGroup();
        // System.out.println(rgroupp.getUserData().toString());
        rgroupp.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (rgroupp.getSelectedToggle() != null) {
                    System.out.println(new_toggle.getUserData().toString());
                    selectedCompres = new_toggle.getUserData().toString();
                }
            }
        });

        /*
        textFieldDirPairs.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textFieldDirPairs changed from " + oldValue + " to " + newValue);
            if (bTextFieldNameUnderRecursiveChange)
                return;
            bTextFieldNameUnderRecursiveChange = true;
            if (existsAllReady(newValue))
                textFieldDirPairs.setText(oldValue);
            else
            {
                mainCI.setName(newValue);
                modifyListItemNamesIntoUnicNames(oldValue, newValue);
                listViewDirs.refresh();
            }
            bTextFieldNameUnderRecursiveChange = false;
        });
         */

        textFieldSourcePath.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textFieldSourcePath changed from " + oldValue + " to " + newValue);
            if (!bTextFieldNameUnderRecursiveChange)
                textFieldDirPairs.setText("");
        });

        textFieldTargetPath.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textFieldSourcePath changed from " + oldValue + " to " + newValue);
            if (!bTextFieldNameUnderRecursiveChange)
                textFieldDirPairs.setText("");
        });
    }

    /*
    private void mousePressedInListView(MouseEvent event)
    {
        // when an user has pressed double click with mouse:
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            event.consume();
            ListViewDirItem objSelected = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
            if (objSelected == null)
                return;
            if (!radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                    && !radioButtonSortSimilarBaseFNames.isSelected()
                    && !radioButtonMissigTitles.isSelected()
                    && !radioButtonSelectCopyingTitles.isSelected())
                return; // do nothing

            String willDeleteFilePath = (String)objSelected.path;
            if (willDeleteFilePath != null && willDeleteFilePath.trim().length() != 0) {
                if (radioButtonSelectCopyingTitles.isSelected()) {
                    File f = new File(willDeleteFilePath);
                    File targetDir = new File(textFieldTargetPath.getText());
                    if (!f.exists())
                        return;
                    try {
                        writeBookFilesIntoTarget(targetDir, f);
                        int ind = listViewDirs.getSelectionModel().getSelectedIndex();
                        if (ind > -1)
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    listitems.remove(ind);
                                    laberMessage.setText("Copyed book: " +f.getAbsolutePath() +" into target dir");
                                    listViewDirs.refresh();
                                }});

                        // dddd
                    }catch (IOException ioe){
                        sbError.append("cannot write " +f.getAbsolutePath() +" into " +targetDir.getAbsolutePath() +"\n");
                    }
                }
                else
                if(radioButtonFindTuples.isSelected())
                    deleletTitleFile(willDeleteFilePath);
            }
            //your code here
        }
        else
        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1 ?* &&
                        (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0) *?) {
            event.consume();
            ListViewDirItem objSelected = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
            if (objSelected == null)
                return;
            ?*
            if (!radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                    && !radioButtonSortSimilarBaseFNames.isSelected() && !radioButtonMissigTitles.isSelected())
                return; // do nothing
            *?
            String willDeleteFilePath = (String)objSelected.path;
            if (willDeleteFilePath != null && willDeleteFilePath.trim().length() != 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textFieldCurrentDir.setText(willDeleteFilePath);
                    }});
            }
        }
        else
            // if an user will see document as open::
            if( event.isSecondaryButtonDown()) {
                event.consume();
                int indSelection = listViewDirs.getSelectionModel().getSelectedIndex();
                if (indSelection == -1)
                    return;
                ListViewDirItem item = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
                if (item == null || item.path == null)
                    return;

                File f = new File(item.path);
                if (!f.exists())
                    return;

                if (!isDocument(f))
                    return;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Desktop.getDesktop().open(f);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
    }
     */

    private Properties getProperties()
    {
        Properties props = new Properties();
        try {
            File f = new File("." +File.separator + cnst_prop_file);
            System.out.println("properties file=" +f.getAbsolutePath());
            FileReader fr = null;
            if (f.exists()) {
                System.out.println("load=" +f.getAbsolutePath());
                fr = new FileReader(f);
            }
            else
            {
                System.out.println("cannot load=: " +f.getAbsolutePath());
                System.exit(1);
            }

            if (fr != null) {
                props.load(fr);
            }
            String envUser = (File.separatorChar == '\\' ? "USERPROFILE" : "HOME");
            String useHome = System.getenv(envUser);
            System.out.println("useHome=" +useHome);
            File userAppDir = new File(useHome +File.separator +".smartfilecopy");
            if (!userAppDir.exists())
                userAppDir.mkdir();
            System.out.println("userAppDir=" +userAppDir);

            File f2 = new File(userAppDir.getAbsolutePath() +File.separator +cnst_prop_file);
            if (f2.exists())
            {
                fr = new FileReader(f2);
                System.out.println("load=" +f2.getAbsolutePath());
                props.load(fr);
            }
            else
            {
                FileOutputStream fos = new FileOutputStream(f2);
                props.store(fos, "application values for a user");
                fos.close();
            }
            String none_empthy_dirs = props.getProperty(cnst_none_empthy_dirs);
            if (none_empthy_dirs != null)
                none_empthy_dirs = none_empthy_dirs.trim();
            if (none_empthy_dirs != null && none_empthy_dirs.contains(cnst_ebooktypes))
            {
                String ebooktypes = props.getProperty(cnst_ebooktypes);
                none_empthy_dirs = none_empthy_dirs.replace("{" +cnst_ebooktypes +"}", ebooktypes);
                props.setProperty(cnst_none_empthy_dirs, none_empthy_dirs);
            }
            System.out.println("loaded ok");
        }catch (Exception e){
            e.printStackTrace();
            return props;
        }
        return props;
    }

    private void mousePressedInListView1Click(MouseEvent event)
    {
            event.consume();
            ListViewDirItem objSelected = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
            if (objSelected == null)
                return;
            /*
            if (!radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                    && !radioButtonSortSimilarBaseFNames.isSelected() && !radioButtonMissigTitles.isSelected())
                return; // do nothing
            */
            String willDeleteFilePath = (String)objSelected.path;
            if (willDeleteFilePath != null && willDeleteFilePath.trim().length() != 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textFieldCurrentDir.setText(willDeleteFilePath);
                    }});
            }
    }

    private void mousePressedInListViewDClick(MouseEvent event)
    {
        // when an user has pressed double click with mouse:
           // event.consume();
            ListViewDirItem objSelected = (ListViewDirItem)listViewDirs.getSelectionModel().getSelectedItem();
            if (objSelected == null)
                return;
            if (!radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                    && !radioButtonSortSimilarBaseFNames.isSelected()
                    && !radioButtonMissigTitles.isSelected()
                    && !radioButtonSelectCopyingTitles.isSelected()
                    && !radioButtonFindRealTuples.isSelected()
                    && !radioButtonSortTypeOf.isSelected())
                return; // do nothing

            String willDeleteFilePath = (String)objSelected.path;
            if (willDeleteFilePath != null && willDeleteFilePath.trim().length() != 0) {
                if (radioButtonSelectCopyingTitles.isSelected()) {
                    File f = new File(willDeleteFilePath);
                    if (!f.exists())
                        return;
                    File fTargetDir = new File(textFieldTargetPath.getText());
                    if (!fTargetDir.exists())
                        return;

                    File fTargetAuthor = new File(fTargetDir.getAbsoluteFile() +File.separator +getAuthDirName(f));
                    if (!fTargetAuthor.exists())
                       if (!fTargetAuthor.mkdir())
                       {
                           updateMessage("Cannot create none existing author dir: " +fTargetAuthor.getAbsolutePath());
                           return;
                       }
                        File ftargetBookDir = new File(fTargetAuthor.getAbsoluteFile() +File.separator +getBookDirName(f));
                        if (!ftargetBookDir.exists())
                            if (!ftargetBookDir.mkdir())
                            {
                                updateMessage("Cannot create none existing book dir: " +ftargetBookDir.getAbsolutePath());
                                return;
                            }

                        try {
                            writeBookFilesIntoTarget(ftargetBookDir, f);
                            int ind = listViewDirs.getSelectionModel().getSelectedIndex();
                            final int newSelectedIdx =
                                    (ind == listViewDirs.getItems().size() - 1)
                                            ? ind - 1
                                            : ind;
                            if (ind > -1)
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    listViewDirs.setItems(null);
                                    listitems.remove(newSelectedIdx);
                                    listViewDirs.setItems(listitems);
                                    listViewDirs.refresh();
                                    // listViewDirs.getItems().remove(ind);
                                    laberMessage.setText("Copyed book: " +f.getAbsolutePath() +" into target dir");
                                }});

                        // dddd
                    }catch (IOException ioe){
                        sbError.append("cannot write " +f.getAbsolutePath() +" into " +ftargetBookDir.getAbsolutePath() +"\n");
                    }
                }
                else
                if(radioButtonSortSimilarBaseFNames.isSelected()
                    || radioButtonSelectCopyingTitles.isSelected()
                    || radioButtonFindTuples.isSelected()
                    || radioButtonMissigTitles.isSelected()
                    || radioButtonFindRealTuples.isSelected())
                    deleletTitleFile(willDeleteFilePath);
            }
    }

    @FXML
    private void radioButtonFindTuplesPrsessed()
    {

    }

    private boolean isDocument(File f2)
    {
        if (f2.getName().endsWith(".epub") || f2.getName().endsWith(".pdf") || f2.getName().endsWith(".mobi")
                || f2.getName().endsWith(".doc") || f2.getName().endsWith(".txt") || f2.getName().endsWith(".docx")
                || f2.getName().endsWith(".htm") || f2.getName().endsWith(".html") || f2.getName().endsWith(".pdb")
                || f2.getName().endsWith(".rar")  || f2.getName().endsWith(".ps") || f2.getName().endsWith(".rb")
                || f2.getName().endsWith(".rtf") || f2.getName().endsWith(".zip") || f2.getName().endsWith(".lz7")
                || f2.getName().endsWith(".jar") || f2.getName().endsWith(".tar") || f2.getName().endsWith(".gz"))
            return true;
        return false;
    }

    private void deleletTitleFile(String willDeleteFilePath)
    {
        if (willDeleteFilePath == null || willDeleteFilePath.trim().length() == 0)
            return;
        File f = new File(willDeleteFilePath);
        if (!f.exists())
        {
            laberMessage.setText("This file does not exists anymore: " +f.getAbsolutePath());
            return;
        }

        bDeleteFile = false;
        ButtonType yesbutton = new ButtonType("YES", ButtonBar.ButtonData.OK_DONE);
        ButtonType nobutton = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "Delete file '" +willDeleteFilePath +"'?",
                yesbutton,
                nobutton);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        alert.setWidth(primaryScreenBounds.getWidth());
        alert.setTitle("Delete this file");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(nobutton) == yesbutton) {
            bDeleteFile = true;
        }

        if (!bDeleteFile)
        {
            laberMessage.setText("The file not deleted.");
            return;
        }

        File parentfile = f.getParentFile();
        if (radioButtonMissigTitles.isSelected() && f.isDirectory())
        {
            File [] files = f.listFiles();
            for(File f2 : files)
            {
                if (f2.exists())
                    f2.delete();
            }
            File parentF = f.getParentFile();
            f.delete();
            if (f.exists())
            {
                laberMessage.setText("Cannot delete dir: " +f.getAbsolutePath());
                return;
            }
            files = parentF.listFiles();
            int iFiles = files.length;
            if (iFiles == 0)
            {
                File deletedir = parentF;
                parentF = parentF.getParentFile();
                if (deletedir.delete())
                    parentF.delete();
            }
            laberMessage.setText("Deleted dir: " +f.getAbsolutePath());
            int indSelected = listViewDirs.getSelectionModel().getSelectedIndex();
            if (indSelected > -1)
            {
                listViewDirs.getItems().remove(indSelected);
            }
            return;
        }
        else
        if (!f.delete())
        {
            laberMessage.setText("Cannot delete this file: " +f.getAbsolutePath());
            return;
        }

        int ind = listViewDirs.getSelectionModel().getSelectedIndex();
        if (ind > -1)
            listitems.remove(ind);

        File [] anotherfiles = parentfile.listFiles();
        File superparent = parentfile.getParentFile();
        if (anotherfiles.length == 0) {
            if (!parentfile.delete()) {
                laberMessage.setText("Cannot delete the parent file: " + parentfile.getAbsolutePath());
                return;
            }
        }

        boolean bDotNotDeleteParent = false;
        for(File f2 : anotherfiles)
        {
            if (f2.getName().endsWith(".epub") || f2.getName().endsWith(".pdf") || f2.getName().endsWith(".mobi")
                || f2.getName().endsWith(".doc") || f2.getName().endsWith(".txt") || f2.getName().endsWith(".docx")
                || f2.getName().endsWith(".htm") || f2.getName().endsWith(".html") || f2.getName().endsWith(".pdb")
                || f2.getName().endsWith(".rar")  || f2.getName().endsWith(".ps") || f2.getName().endsWith(".rb")
                || f2.getName().endsWith(".rtf") || f2.getName().endsWith(".zip") || f2.getName().endsWith(".lz7")
                || f2.getName().endsWith(".jar") || f2.getName().endsWith(".tar") || f2.getName().endsWith(".gz") )
                bDotNotDeleteParent = true;
        }
        if (bDotNotDeleteParent)
            return;

        File fPicture = new File(parentfile.getAbsolutePath() +File.separator +"cover.jpg");
        if (fPicture.exists())
            if (!fPicture.delete())
            {
                sbError.append("Cannot delete picture: " +fPicture.getAbsolutePath());
            }
        File fMetaData = new File(parentfile.getAbsolutePath() +File.separator +"metadata.opf");
        if (fMetaData.exists()) {
            if (!fMetaData.delete()) {
                sbError.append("Cannot delete metafile: " + fMetaData.getAbsolutePath());
            }
        }
        anotherfiles = parentfile.listFiles();
        if (anotherfiles != null && anotherfiles.length != 0)
             return;
        /*
        for(File f2 : anotherfiles)
        {
            if (!f2.delete()){
                laberMessage.setText("Cannot delete the parent file: " + f2.getAbsolutePath());
                return;
            }
        }
         */
        if (!parentfile.delete()) {
            laberMessage.setText("Cannot delete the parent file: " + parentfile.getAbsolutePath());
            return;
        }
        if (superparent.exists())
        {
            File [] children = superparent.listFiles();
            if (children.length == 0)
                if (!superparent.delete()) {
                    laberMessage.setText("Cannot delete the parent file: " + superparent.getAbsolutePath());
                    return;
                }
        }
    }

    private boolean existsAllReady(String newValue)
    {
        boolean ret = false;
        if (newValue == null || newValue.trim().length() == 0)
            return false;   
        String [] items = new String[listitems.size()];
        items = (String [])listitems.toArray(items);
        for (String ci : items)
        {
            if (ci.equals(newValue))
                return true;
        }
        return ret;
    }

    public void appIsClosing()
    {
        saveConfigs();
    }

    private void saveConfigs()
    {
        updateMainCiAndListItems();
        if (readItems != null)
            ConfigDirItem.writeJsonFile(this.useConfigFile, readItems, mainCI);
    }

    private void updateMainCiAndListItems()
    {
        String name = textFieldDirPairs.getText();
        if (name == null || name.trim().length() == 0)
            name  = this.cnstDefaultPairName;
        mainCI = new ConfigDirItem(name, textFieldSourcePath.getText(), textFieldTargetPath.getText());
        boolean bTheSameMainCl = false;
        int i = -1, iTheSameMainCl = -1;
        if (readItems == null)
        {
            if (mainCI != null) {
                readItems = new ConfigDirItem[1];
                readItems[0] = mainCI;
            }
        }

        if (readItems != null)
        for(ConfigDirItem c : readItems)
        {
            i++;
            if (c.getName().equals(mainCI.getName())) {
                bTheSameMainCl = true;
                iTheSameMainCl = i;
            }
        }
        if (!bTheSameMainCl && readItems != null && readItems.length >0) {
            ConfigDirItem [] newItems = new ConfigDirItem[readItems.length +1];
            i = 0;
            for(ConfigDirItem c : readItems)
                newItems[i] = readItems[i++];
            newItems[i] = mainCI;
        }
        else
        {
            if (bTheSameMainCl && iTheSameMainCl > -1)
                readItems[iTheSameMainCl] = mainCI;
        }
    }

    /*
    private void modifyListItemNamesIntoUnicNames(String oldname, String newname)
    {
        ConfigDirItem [] items = new ConfigDirItem [listViewDirs.getItems().size()];
        items = (ConfigDirItem [])listViewDirs.getItems().toArray(items);
    }
     */

    @FXML
    protected void buttonCompareDirsClicked()
    {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setResizable(true);
        dialog.setTitle("Edit Copy Title dirs");
        //Adding buttons to the dialog pane
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("comparetitlesdirs.fxml"));
        compareDirsController = new CompareDirsController();
        fxmlLoader.setController(compareDirsController);
        try {
            Parent loadedroot = fxmlLoader.load();
            dialogPane.setContent((Node)loadedroot);
            dialog.setDialogPane(dialogPane);
            dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                public void handle(DialogEvent we) {
                    System.out.println("Dialog is closing");
                }
            });

            ButtonType loginButtonType = new ButtonType("Close", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(loginButtonType);
            boolean disabled = false; // computed based on content of text fields, for example
            dialog.getDialogPane().lookupButton(loginButtonType).setDisable(disabled);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {

            }
        }catch (Exception e){
            e.printStackTrace();
            laberMessage.setText(e.getMessage());
        }
    }

    public static String getRootDir()
    {
        if (File.separatorChar == '\\')
            return "c:\\";
        return "/";
    }

    public void buttonDirPairsClicked()
    {
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setResizable(true);
        dialog.setTitle("Config: named source and target dirs");
        //Adding buttons to the dialog pane
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selectconfig.fxml"));
        String envUser = (File.separatorChar == '\\' ? "USERPROFILE" : "HOME");
        String useHome = System.getenv(envUser);
        File userAppDir = new File(useHome +File.separator +".smartfilecopy");
        if (!userAppDir.exists())
            if (!userAppDir.mkdir()) {
                laberMessage.setText("Cannot create dir: " +userAppDir.getAbsolutePath());
                return;
            }

        updateMainCiAndListItems();

        File useConfigFile = new File(userAppDir.getAbsolutePath() +File.separator +"config.json");
        configController = new ConfigController(useConfigFile, readItems,
                textFieldDirPairs.getText(), textFieldSourcePath.getText(), textFieldTargetPath.getText());
        configController.setPrimaryStage(this.primaryStage);
        configController.setSeltectedConfigDirItem(mainCI);
        fxmlLoader.setController(configController);
        try {
            Parent loadedroot = fxmlLoader.load();
            dialogPane.setContent((Node)loadedroot);
            dialog.setDialogPane(dialogPane);
            dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
                public void handle(DialogEvent we) {
                    System.out.println("Dialog is closing");
                    if (mainCI == null && configController.getSelectedConfigDirItem() == null) {
                        mainCI = new ConfigDirItem(textFieldDirPairs.getText(),
                                textFieldSourcePath.getText(), textFieldTargetPath.getText());
                        configController.setSeltectedConfigDirItem(mainCI);
                        bTextFieldNameUnderRecursiveChange = true;
                        textFieldDirPairs.setText(mainCI.getName());
                        bTextFieldNameUnderRecursiveChange = false;
                    }
                    else {
                        mainCI = configController.getSelectedConfigDirItem();
                        if (mainCI != null) {
                            bTextFieldNameUnderRecursiveChange = true;
                            textFieldDirPairs.setText(mainCI.getName());
                            textFieldTargetPath.setText(mainCI.getTarget());
                            textFieldSourcePath.setText(mainCI.getSource());
                            bTextFieldNameUnderRecursiveChange = false;
                        }
                    }
                    ConfigDirItem [] ret = configController.getReadItems();
                    if (ret != null) {
                        List<ConfigDirItem> listItems = new ArrayList<ConfigDirItem>();
                        for(ConfigDirItem c : ret)
                        {
                            if (c.getName().trim().length() == 0 && c.getSource().trim().length() == 0
                                && c.getTarget().trim().length() == 0)
                                continue;
                            listItems.add(c);
                        }
                        ret = new ConfigDirItem[listItems.size()];
                        ret = listItems.toArray(ret);
                        readItems = ret;
                    }
//                    useConfigFile = new File(userAppDir.getAbsolutePath() +File.separator +"config.json");
                    ConfigDirItem.writeJsonFile(useConfigFile, readItems, mainCI);
                    mainCI = configController.getSelectedConfigDirItem();
                    if (mainCI != null) {
                        bTextFieldNameUnderRecursiveChange = true;
                        textFieldDirPairs.setText(mainCI.getName());
                        textFieldTargetPath.setText(mainCI.getTarget());
                        textFieldSourcePath.setText(mainCI.getSource());
                        bTextFieldNameUnderRecursiveChange = false;
                    }
                }
            });

            ButtonType loginButtonType = new ButtonType("Close", ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(loginButtonType);
            boolean disabled = false; // computed based on content of text fields, for example
            dialog.getDialogPane().lookupButton(loginButtonType).setDisable(disabled);
            Optional<ButtonType> result = dialog.showAndWait();
            /*
            if (result.isPresent() && result.get() != null) {
                ConfigDirItem cdiritem = configController.getSelectedConfigDirItem();
                if (cdiritem == null)
                    return;
                textFieldDirPairs.setText(cdiritem.getName());
                textFieldSourcePath.setText(cdiritem.getSource());
                textFieldTargetPath.setText(cdiritem.getTarget());
            }
             */
        }catch (Exception e){
            e.printStackTrace();
            laberMessage.setText(e.getMessage());
        }
    }

    public void buttonShowErrorsClicked()
    {
        Dialog dialog = new Dialog();
        dialog.setTitle("Errors");
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double width = primaryScreenBounds.getWidth();
        dialog.setWidth(width);
        dialog.setResizable(true);
        double heigth = primaryScreenBounds.getHeight();
        dialog.setHeight(heigth);
        ButtonType type = new ButtonType("Close", ButtonData.OK_DONE);
        ScrollPane scrollPane = new ScrollPane();
        ListView<String> listView = new ListView<String>();
        listView.getItems().addAll(sbError.toString().split("\n"));
        scrollPane.setContent(listView);
        dialog.getDialogPane().setContent(scrollPane);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait();
    }

    private void sortAndShowBaseFileNames()
    {
        // Sort files by name
        buttonExecute.setDisable(true);
        fileList = new ArrayList<File>();
        File targetDir = new File(textFieldTargetPath.getText());
        strSourceTarget = new String("Target");
        if (!radioButtonSource.isDisabled() && radioButtonSource.isSelected()) {
            targetDir = new File(textFieldSourcePath.getText());
            strSourceTarget = new String("Source");
        }
        if (!targetDir.exists())
        {
            final String path = targetDir.getAbsolutePath();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    laberMessage.setText(strSourceTarget +" dir does not exists: " + path);
                }});
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laberMessage.setText("Starting sorting of file names afeter basenames");
                labelList.setText("Sorted ebook list after file basename. Open a file with right mouse button. Delete a file with double mouse click.");
            }});

        seekSortedTargetDirBooks(targetDir);
        File [] arrFileList = new File[fileList.size()];
        arrFileList = fileList.toArray(arrFileList);
        Arrays.sort(arrFileList, NameFileComparator.NAME_COMPARATOR);
        // Collections.sort(fileList);
        listitems.clear();
        ListViewDirItem item = null;
        for(File f : arrFileList)
        {
            item = new ListViewDirItem();
            item.showname = f.getAbsolutePath();
            item.path = item.showname;
            item.title = getToolTipTitle(f);
            updateMessage(getStrikeChars());
            listitems.add(item);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laberMessage.setText("Sorted done");
                buttonExecute.setDisable(false);
            }});
    }

    private void sortAndShowSimilarBaseFileNames()
    {
        bSimilarEbooksFounded = false;
        // Sort files by name
        buttonExecute.setDisable(true);
        fileList = new ArrayList<File>();
        File targetDir = new File(textFieldTargetPath.getText());
        strSourceTarget = new String("Target");
        if (!radioButtonSource.isDisabled() && radioButtonSource.isSelected()) {
            targetDir = new File(textFieldSourcePath.getText());
            strSourceTarget = new String("Source");
        }
        if (!targetDir.exists())
        {
            final String path = targetDir.getAbsolutePath();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    laberMessage.setText(strSourceTarget +" dir does not exists: " + path);
                }});
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laberMessage.setText("Starting sorting of file names afeter basenames");
                labelList.setText("Sorted ebook list after file basename. Open a file with right mouse button. Delete a file with double mouse click.");
            }});
        seekSortedTargetDirBooks(targetDir);
        File [] arrFileList = new File[fileList.size()];
        arrFileList = fileList.toArray(arrFileList);
        final char chStrike = '-';

        Arrays.sort(arrFileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                String basename1, basename2, first1, second1, second2, first2;
                String authorpath1 = null, authorpath2 = null;
                String [] arr_authorpath1, arr_authorpath2;
                int indExt1, indExt2, ind1, ind2;
                updateMessage(getStrikeChars());
                basename1 = f1.getName();
                basename2 = f2.getName();
                arr_authorpath1 = f1.getAbsolutePath().split(File.separatorChar == '\\' ? "\\\\": "/");
                arr_authorpath2 = f2.getAbsolutePath().split(File.separatorChar == '\\' ? "\\\\": "/");
                authorpath1 = arr_authorpath1[arr_authorpath1.length-3];
                authorpath2 = arr_authorpath2[arr_authorpath2.length-3];
                if (basename1.contains(authorpath1))
                    basename1 = basename1.replace(authorpath1, "");
                if (basename2.contains(authorpath2))
                    basename2 = basename2.replace(authorpath2, "");
                if (basename1.contains(""+chStrike))
                    basename1 = basename1.replace(""+chStrike, "");
                if (basename2.contains(""+chStrike))
                    basename2 = basename2.replace(""+chStrike, "");

                indExt1 = basename1.lastIndexOf('.');
                indExt2 = basename2.lastIndexOf('.');
                if (indExt1 > -1)
                    basename1 = basename1.substring(0, indExt1);
                if (indExt2 > -1)
                    basename2 = basename2.substring(0, indExt2);
                basename1 = basename1.trim();
                basename2 = basename2.trim();
                if (basename1.equals(basename2))
                    return 0;
                else {
                    ind1 =  basename1.indexOf(chStrike);
                    ind2 =  basename2.indexOf(chStrike);
                    if (ind1 > -1 && ind2 > -1) {
                        first1 = basename1.substring(0, ind1).trim();
                        second1 = basename1.substring(ind1 + 1).trim();
                        second2 = basename2.substring(ind2).trim();
                        first2 = basename2.substring(0, ind2).trim();
                        if (first1 != null && second1 != null && second2 != null && first2 != null
                                && first1.equals(second2) && second1.equals(first2)) {
                            bSimilarEbooksFounded = true;
                            return 0;
                        }
                        else
                            return basename1.compareTo(basename2);
                    }
                    else
                        return basename1.compareTo(basename2);
                }
            }
        });
        // Collections.sort(fileList);
        listitems.clear();
        for(File f : arrFileList)
        {
            ListViewDirItem item = new ListViewDirItem();
            item.showname = f.getAbsolutePath();
            item.path = item.showname;
            item.title = getToolTipTitle(f);
            listitems.add(item);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laberMessage.setText("Sorted done");
            }});
    }

    private ExtensionsFilter getEbooksFilter()
    {
        ExtensionsFilter ret = new ExtensionsFilter(new String[] {".epub", ".pdf",".mobi"});
        String value = proerties.getProperty(cnst_ebooktypes);
        if (value != null && value.trim().length() > 0)
        {
            String [] arrValues = getAddPointCharArray(value.split(","));
            int iValues = arrValues.length;
            if (iValues > 0)
            {
                ret = new ExtensionsFilter(arrValues);
            }
        }
        return ret;
    }

    private String [] getAddPointCharArray(String [] arr)
    {
        String [] ret = arr;
        if (arr != null && arr.length > 0)
        {
            int i = 0;
            for(String value : arr)
            {
                if (value != null && !value.startsWith("."))
                    ret[i] = "." +value.trim();
                i++;
            }
        }
        return ret;
    }

    private ExtensionsFilter getAllEbooksFilter()
    {
        ExtensionsFilter ret = new ExtensionsFilter(new String[] {".epub", ".pdf",".mobi",".zip"});
        String value = proerties.getProperty(cnst_none_empthy_dirs);
        if (value != null && value.trim().length() > 0)
        {
            String [] arrValues = getAddPointCharArray(value.split(","));
            int iValues = arrValues.length;
            if (iValues > 0)
            {
                ret = new ExtensionsFilter(arrValues);
            }
        }
        return ret;
    }

    public void buttonExecutePressed()
    {
        bSimilarEbooksFounded = false;

        double d = dSimilar;
        try {
            dSimilar = Double.valueOf(textFiledSimilarity.getText());
            if (dSimilar < 0.0) {
                dSimilar = 0.0;
                textFiledSimilarity.setText("" + dSimilar);
            }
        }catch (Exception e){
            dSimilar = d;
        }

        String strSourcePath = this.textFieldSourcePath.getText();
        if (strSourcePath == null || strSourcePath.trim().length() < 1) {
            laberMessage.setText("Source textField is empty!");
            return;
        }
        String strTargetPath = this.textFieldTargetPath.getText();
        if (strTargetPath == null || strTargetPath.trim().length() < 1) {
            laberMessage.setText("Target textField is empty!");
            return;
        }
        File fSourceDir = new File(strSourcePath);
        if (!radioButtonFindRealTuples.isSelected() && !radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected() && !fSourceDir.exists()) {
            laberMessage.setText("Source dir does not exists!");
            return;
        }
        if (!radioButtonFindRealTuples.isSelected() && !radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected() && !fSourceDir.isDirectory())
        {
            laberMessage.setText("Source dir is a file!");
            return;
        }
        File fTargetDir = new File(strTargetPath);
        if (!fTargetDir.exists())
        {
            laberMessage.setText("Target dir does not exists!");
            return;
        }
        if (!fTargetDir.isDirectory())
        {
            laberMessage.setText("Target dir is a file!");
            return;
        }
        if (!radioButtonFindRealTuples.isSelected() && !radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                && fSourceDir.getAbsolutePath().equals(fTargetDir.getAbsolutePath()))
        {
            laberMessage.setText("Target dir and source dir are the same!. Stopped");
            return;
        }

        if (!radioButtonTar.isSelected() && !radioButton7z.isSelected()
                && !radioButtonZip.isSelected()
                && !radioButtonCopyTitles.isSelected()
                && !radioButtonSelectCopyingTitles.isSelected()
                && !radioButtonCopyFlles.isSelected()
                && !radioButtonFindTuples.isSelected() && !radioButtonSortBaseFNames.isSelected()
                && !radioButtonSortSimilarBaseFNames.isSelected()
                && !radioButtonMissigTitles.isSelected()
                && !radioButtonFindRealTuples.isSelected()
                && !radioButtonSortTypeOf.isSelected())
        {
            laberMessage.setText("Select some of radio buttons before execution!");
            return;
        }

        proerties = getProperties();
        ebookfilter = getEbooksFilter();
        ebookfilterAll = getAllEbooksFilter();

        String strChoiseValue = (String)choiceboxType.getSelectionModel().getSelectedItem();
        choiceboxType.getItems().clear();
        choiceboxType.getItems().add("-- all");
        for(String f : getAddPointCharArray(proerties.get(cnst_none_empthy_dirs).toString().split(",")))
            choiceboxType.getItems().add(f.trim());
        if (strChoiseValue != null)
            choiceboxType.getSelectionModel().select(strChoiseValue);

        sbError = new StringBuffer();
        listitems.clear();
        listViewDirs.refresh();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        labelStartTime.setText(dtf.format(now));
        labelEndTime.setText("");
        laberMessage.setText("Started execution...");

        task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                buttonExecute.setDisable(true);
                buttonShowErrors.setDisable(true);
                bTaskExecuting = true;
                listViewDirs.setItems(null);
             //   labelEndTime.setText(dtf.format(now));
             //   labelEndTime.setText(dtf.format(now));
                    }});

                try {
                    if (radioButtonCopyTitles.isSelected() || radioButtonSelectCopyingTitles.isSelected())
                        sendBooksIntoDevice(fSourceDir, fTargetDir, arrCompareDirs);
                    else {
                        if (radioButtonFindTuples.isSelected() || radioButtonFindRealTuples.isSelected())
                            findTuplesTitleFromTarget();
                        else
                        if (selectedCompres != null)
                            startFileCompress(fSourceDir, fTargetDir);
                        else
                        if (radioButtonCopyFlles.isSelected())
                            startFileCopying(fSourceDir, fTargetDir);
                        else
                        if (radioButtonSortBaseFNames.isSelected())
                            sortAndShowBaseFileNames();
                        else
                        if (radioButtonSortSimilarBaseFNames.isSelected())
                            sortAndShowSimilarBaseFileNames();
                        else
                        if (radioButtonMissigTitles.isSelected())
                            searchMissingTitles();
                        else
                        if (radioButtonSortTypeOf.isSelected())
                            startSearchSpesificTypeOfFiles();
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            listViewDirs.setItems(listitems);
                        }});

                }catch (IOException ioe) { // all ready handled
                    ioe.printStackTrace();
                    return 1;
                }
                LocalDateTime now2 = LocalDateTime.now();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        LocalDateTime now = LocalDateTime.now();
                        labelEndTime.setText(dtf.format(now2));
                    if (sbError.toString().length()> 0) {
                        if (laberMessage.getText().trim().length() == 0) {
                            laberMessage.setStyle("-fx-background-color: red");
                            laberMessage.setText("Errors occurred during the execute! Press show errors button");
                        }
                        buttonShowErrors.setDisable(false);
                    }
                    else {
                        if (listitems.size() == 0)
                            laberMessage.setText("The list is empty");
                        else
                        {
                            if (bSimilarEbooksFounded)
                                laberMessage.setText("execution done: some similar ebook founded!");
                            else
                            if (radioButtonSortSimilarBaseFNames.isSelected())
                                laberMessage.setText("execution done: No similar ebook founded!");
                            else
                                laberMessage.setText("execution done");

                        }
                    }
                }});
                super.succeeded();
                return 0;
            }

            @Override
            protected void succeeded() {
                /*
                laberMessage.setText("All done1: ");
                */
                buttonExecute.setDisable(false);
                bTaskExecuting = false;
            }

            @Override
            protected void cancelled() {
                super.cancelled();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listViewDirs.setItems(listitems);
                        laberMessage.setText("Canceled!");
                        buttonExecute.setDisable(false);
                        bTaskExecuting = false;
                    }});
            }

            @Override
            protected void failed() {
                super.failed();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listViewDirs.setItems(listitems);
                        laberMessage.setText("Failed!");
                        buttonExecute.setDisable(false);
                        bTaskExecuting = false;
                    }});
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private void searchSpesificTypeOfFiles(File fTargetDir, ExtensionsFilter ebookfilter) {
        File [] ebooks = fTargetDir.listFiles(ebookfilter);
        File [] subdirs = fTargetDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        ListViewDirItem item;
        for (File f : ebooks) {
            /*
            item = new ListViewDirItem();
            item.showname = f.getAbsolutePath();
            item.path = item.showname;
            item.title = getToolTipTitle(f);
            listitems.add(item);
             */
            fileList.add(f);
        }

        /*
        if ((subdirs == null || subdirs.length == 0) && ebooks.length == 0)
        {
            System.out.println("kissa");
        }
         */
        for(File dir : subdirs)
        {
            searchSpesificTypeOfFiles(dir, ebookfilter);
        }
    }

    private void startSearchSpesificTypeOfFiles()
    {
        String selectedType = (String)choiceboxType.getSelectionModel().getSelectedItem();
        ExtensionsFilter ebookfilter = null;
        listitems = FXCollections.observableArrayList();
        listViewDirs.setItems(listitems);

        if (selectedType != null && selectedType.startsWith("-- all")) {
            selectedType = null;
            ebookfilter = ebookfilterAll;
        }
        else
        {
            ebookfilter = new ExtensionsFilter(new String[] { selectedType });
        }
        File targetDir = new File(textFieldTargetPath.getText());
        strSourceTarget = new String("Target");
        if (!radioButtonSource.isDisabled() && radioButtonSource.isSelected()) {
            targetDir = new File(textFieldSourcePath.getText());
            strSourceTarget = new String("Source");
        }

        try {
            fileList = new ArrayList<File>();
            searchSpesificTypeOfFiles(targetDir, ebookfilter);
            File [] arrFileList = new File[fileList.size()];
            arrFileList = fileList.toArray(arrFileList);
            Arrays.sort(arrFileList, NameFileComparator.NAME_COMPARATOR);
            // Collections.sort(fileList);
            listitems.clear();
            ListViewDirItem item = null;
            for(File f : arrFileList)
            {
                item = new ListViewDirItem();
                item.showname = f.getAbsolutePath();
                item.path = item.showname;
                item.title = getToolTipTitle(f);
                updateMessage(getStrikeChars());
                listitems.add(item);
            }

            updateMessage("Executtion ready.");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelList.setText("Open a directory with right mouse button.");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            updateMessage(e.getMessage());
        }
    }

    private void searchMissingTitles()
    {
        hmSourceBooks = new HashMap<String,FoundedHashFiles>();
        hmTargetBooks = new HashMap<String,FoundedHashFiles>();
        hmCompareBooks = new HashMap<String,FoundedHashFiles>();
        File targetDir = new File(textFieldTargetPath.getText());
        strSourceTarget = new String("Target");
        if (!radioButtonSource.isDisabled() && radioButtonSource.isSelected()) {
            targetDir = new File(textFieldSourcePath.getText());
            strSourceTarget = new String("Source");
        }

        dirsMissingTitle = new ArrayList<File>();
        boolean bRootLevel = true;
        seekMissingTargetDirBooks(targetDir, bRootLevel);
        if (dirsMissingTitle.size() == 0)
            updateMessage("No empty book dirs!");
        else
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelList.setText("Empty doc directories. Open a directory with right mouse button. Delete a directory with double mouse click.");
                }});
            ListViewDirItem listItem;
            listitems.clear();
            for(File f : dirsMissingTitle)
            {
                listItem = new ListViewDirItem();
                listItem.title = " Empty dir: " +f.getAbsolutePath();
                listItem.path = f.getAbsolutePath();
                listItem.showname = " 'Empty' dir: " +f.getAbsolutePath();
                listitems.add(listItem);
            }
        }
    }

    private String getFileExtension(String fname)
    {
        int ind = fname.lastIndexOf(".");
        if (ind == -1)
            return "";
        String ret = fname.substring(ind);
        return ret;
    }

    private FoundedHashFiles getTupleItemInSameFormat(FoundedHashFiles item)
    {
        HashMap<String, Integer> extensions = new HashMap<String, Integer>();
        HashMap<String, FoundedHashFiles> extensionsFF= new HashMap<String, FoundedHashFiles>();
        Integer iCounter;
        String strExttension;
        for(File f : item.arrFiles)
        {
            strExttension = getFileExtension(f.getName());
            iCounter = extensions.get(strExttension);
            if (iCounter == null)
                iCounter = new Integer(0);
            iCounter++;
            extensions.put(strExttension, iCounter);
            extensionsFF.put(strExttension, item);
        }

        FoundedHashFiles ret = null;
        for (String key : extensions.keySet())
        {
            iCounter = extensions.get(key);
            if (iCounter > 1)
            {
                ret = extensionsFF.get(key);
                break;
            }
        }

        return ret;
    }

    private HashMap<String, FoundedHashFiles> getOnlyTuplesInSameFormat(HashMap<String, FoundedHashFiles> hmTuplesBooks)
    {
        HashMap<String, FoundedHashFiles> ret = new HashMap<String, FoundedHashFiles>();
        FoundedHashFiles item, tupleitem;
        for (String key : hmTuplesBooks.keySet())
        {
            item = hmTuplesBooks.get(key);
            if (item == null)
                continue;
            tupleitem = getTupleItemInSameFormat(item);
            if (tupleitem == null)
                continue;
            ret.put(key, item);
        }

        return ret;
    }
    
    private void findTuplesTitleFromTarget()
    {
        File targetDir = new File(textFieldTargetPath.getText());
        strSourceTarget = new String("Target");
        if (!radioButtonSource.isDisabled() && radioButtonSource.isSelected()) {
            targetDir = new File(textFieldSourcePath.getText());
            strSourceTarget = new String("Source");
        }
        if (!targetDir.exists())
        {
            final String path = targetDir.getAbsolutePath();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    laberMessage.setText("Cannot exucute. " +strSourceTarget +" dir not eixsts: " + path);
                }});
            return;
        }
        hmTargetBooks = new HashMap<String,FoundedHashFiles>();
   //     try {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelList.setText("Find same ebook titles from a dir. Open file with right mouse button. Double click will delete file.");
            }});
         seekTargetDirBooks(targetDir);

         HashMap<String, FoundedHashFiles> hmTuplesBooks = new HashMap<String, FoundedHashFiles>();
         FoundedHashFiles fondedTitle;
         for (String title : hmTargetBooks.keySet())
         {
            // if (title == null)
            // continue;
            fondedTitle = hmTargetBooks.get(title);
            if (fondedTitle == null)
                 continue;
            if (fondedTitle.arrFiles.length > 1)
                hmTuplesBooks.put(title, fondedTitle);
        }

        if (radioButtonFindRealTuples.isSelected() && hmTuplesBooks.entrySet().size() > 0)
            hmTuplesBooks = getOnlyTuplesInSameFormat(hmTuplesBooks);

        if (hmTuplesBooks.size() == 0) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    laberMessage.setText("No tuple books founded.");
                }});
            return;
        }

        listitems.clear();
        ListViewDirItem item;
        for (String title : hmTuplesBooks.keySet()) {
          // if (title == null)
          // continue;
            fondedTitle = hmTargetBooks.get(title);
            if (fondedTitle == null)
              continue;
            for (File f : fondedTitle.arrFiles) {
             if (f == null)
                continue;
                item = new ListViewDirItem();
                item.showname = f.getAbsolutePath();
                item.path = item.showname;
                item.title = getToolTipTitle(f);
                listitems.add(item);
            }
            item = new ListViewDirItem();
            item.showname = " ";
            listitems.add(item);
        }

        if (listitems.size() > 0)
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    laberMessage.setText("Tuples books founded, in the list.");
                }});
           // laberMessage.setText("Done!");

     /*   }catch (IOException ioe){
            laberMessage.setText("Error occured: " +ioe.getMessage());
        } */
    }

    private void sendBooksIntoDevice(File fSourceDir, File fTargetDir, File [] arrCompareDirs)
    {
        try {
            hmSourceBooks = new HashMap<String,FoundedHashFiles>();
            hmTargetBooks = new HashMap<String,FoundedHashFiles>();
            hmCompareBooks = new HashMap<String,FoundedHashFiles>();

            bTaskExecuting = true;
            buttonExecute.setDisable(true);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (radioButtonSelectCopyingTitles.isSelected())
                        labelList.setText("Copy only selected ebook titles from source to target. Double click to copy a file. Right click to see inside of a book..");
                    else
                        labelList.setText("Copy ebook titles from source to target. Below listed copied files.");
                }});

          //  if (labelList.getText().trim().length() == 0) {
            seekSourceDirBooks(fSourceDir);
            seekCompareDirBooks(arrCompareDirs);
            seekTargetDirBooks(fTargetDir);
            File[] copyBookFiles = getSelectWhichBooksWillCopyed();
            if (radioButtonSelectCopyingTitles.isSelected())
            {
                // ddd
                listitems.clear();
                ListViewDirItem item;
                for(File f : copyBookFiles)
                {
                   // dddd
                    item = new ListViewDirItem();
                    item.showname = f.getAbsolutePath();
                    item.path = f.getAbsolutePath();
                    item.title = getToolTipTitle(f);
                    listitems.add(item);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        listViewDirs.setItems(listitems);
                }});
            }
            else
                copySelectedBookIntoDevice(copyBookFiles);

            /*
            File pdffile = new File("C:\\Users\\tkassila\\Documents\\Calibre Library\\Andres Almiray, Danno Ferrin, James\\Griffon in Action (438)\\Griffon in Action - Andres Almiray, Danno Ferrin, J.pdf");
            File pdffile2 = new File("C:\\Users\\tkassila\\Documents\\Calibre Library\\Alex Soto Bueno\\Quarkus Cookbook (935)\\Quarkus Cookbook - Alex Soto Bueno.epub");
            String title = getTitle(pdffile2);
            System.out.println("title = " +title);
             */
        //    }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getAuthDirName(File f)
    {
        if (f == null)
            return "";
        File parent = f.getParentFile();
        if (parent == null)
            return "";
        String author = parent.getParentFile().getName();
        return author;
    }

    private String getBookDirName(File f)
    {
        if (f == null)
            return "";
        File parent = f.getParentFile();
        if (parent == null)
            return "";
        String bookdirname = parent.getName();
        return bookdirname;
    }

    private boolean addIntoListItems()
    {
        return !radioButtonSelectCopyingTitles.isSelected();
    }

    private void writeBookFilesIntoTarget(File ftargetBookDir, File f)
            throws IOException
    {
        if (ftargetBookDir == null || !ftargetBookDir.exists())
            return;
        if (f.getName().equals("0130801046 - Unknown.pdf"))
        {
            System.out.println("kissa");
        }
        ListViewDirItem item;
        File fsourceDir = f.getParentFile();
        File [] allsubfiles = fsourceDir.listFiles();
        int iallsubdirs = allsubfiles.length;
        if (iallsubdirs == 0)
            return;
        File imageFile = getImageFile(allsubfiles);
        File metadataFile = getMetadataFile(allsubfiles);
        if (imageFile != null && imageFile.exists()) {
            if (!skipSourceFile(imageFile, ftargetBookDir))
            {
                item = new ListViewDirItem();
                item.showname = "image " +imageFile.getAbsolutePath() +" ...";
                item.path = imageFile.getAbsolutePath();
                item.title = getToolTipTitle(imageFile);
                if (addIntoListItems())
                    listitems.add(item);
                copyFileIntoTargetDir(imageFile, ftargetBookDir);
                item = new ListViewDirItem();
                item.showname = "   Ok" ;
                if (addIntoListItems())
                    listitems.add(item);
            }
        }
        else {
            try {
                item = new ListViewDirItem();
                item.showname = "image 2 " + f.getAbsolutePath() + " ...";
                item.path = f.getAbsolutePath();
                item.title = getToolTipTitle(f);
                if (addIntoListItems())
                    listitems.add(item);
                copyImageIntoTargetDir(f, ftargetBookDir);
                item = new ListViewDirItem();
                item.showname = "   Ok";
                if (addIntoListItems())
                    listitems.add(item);
            }catch (Exception e){
                item = new ListViewDirItem();
                item.showname = "   False";
                if (addIntoListItems())
                    listitems.add(item);
            }
        }
        if (metadataFile != null && metadataFile.exists())
        {
            if (!skipSourceFile(metadataFile, ftargetBookDir)) {
                item = new ListViewDirItem();
                item.showname = "meta " + metadataFile.getAbsolutePath() + " ...";
                item.path = f.getAbsolutePath();
                item.title = getToolTipTitle(f);
                if (addIntoListItems())
                    listitems.add(item);
                copyFileIntoTargetDir(metadataFile, ftargetBookDir);
                item = new ListViewDirItem();
                item.showname = "   Ok";
                if (addIntoListItems())
                    listitems.add(item );
            }
        }
        else {
            // TODO: else !!
            item = new ListViewDirItem();
            item.showname = "meta 2 " + f.getAbsolutePath() + " ...";
            item.path = f.getAbsolutePath();
            item.title = getToolTipTitle(f);
            if (addIntoListItems())
                listitems.add(item);
            copyMetaDataIntoTargetDir(f, ftargetBookDir);
            // listitems.add("   Ok");
        }
        if (!skipSourceFile(f, ftargetBookDir)) {
            item = new ListViewDirItem();
            item.showname = "doc " + f.getAbsolutePath() + " ...";
            item.path = f.getAbsolutePath();
            item.title = getToolTipTitle(f);
            if (addIntoListItems())
                listitems.add(item);
            copyFileIntoTargetDir(f, ftargetBookDir);
            item = new ListViewDirItem();
            item.showname = "   Ok";
            if (addIntoListItems())
                listitems.add(item);
        }
    }

    private void copyImageIntoTargetDir(File f, File ftargetBook)
            throws IOException
    {
        if (f.getName().toLowerCase().endsWith(".pdf"))
        {
            PDDocument pdfdoc = PDDocument.load(f);
            // for(PDPage page : pdfdoc.getPages())
            PDPage coverpage = pdfdoc.getPage(0);
            if (coverpage == null)
            {
                sbError.append("Cannot find a cover page for pdf: " +f.getAbsolutePath() +"\n");
                return;
            }

            Iterable<COSName> objectNames = coverpage.getResources().getXObjectNames();
            BufferedImage imageBuffer = null;
            for (COSName imageObjectName : objectNames) {
                if (coverpage.getResources().isImageXObject(imageObjectName)) {
                    PDImageXObject xObject = (PDImageXObject) coverpage.getResources().getXObject(imageObjectName);
                    imageBuffer = xObject.getImage();
                    break;
                }
            }
            if (imageBuffer != null)
            {
                String strExt = "jpg";
                String outputfilename = "cover." +strExt;
                File targetCover = new File(ftargetBook.getAbsolutePath() +File.separator +outputfilename);
                try {
                    ImageIO.write(imageBuffer, strExt, targetCover);
                } catch (IOException ioe){
                    sbError.append("Cannot write into cover image: " +targetCover.getAbsolutePath() +" from " +f.getAbsolutePath() +"\n");
                    throw ioe;
                }
            }
            else
                sbError.append("Cannot find a cover image for pdf: " +f.getAbsolutePath() +"\n");
            return;
        }
        else
            System.out.println("kissa2");

        String coverfname = getEpubCoverFileName(f);
        if (coverfname == null || coverfname.trim().length()==0)
            return;
        byte [] coverbyte = getEpubCover(f);
        if (coverbyte == null || coverbyte.length ==0)
            return;
        File targetCover = new File(ftargetBook.getAbsolutePath() +File.separator +coverfname);
        if (targetCover.exists())
        {
            sbError.append("Taret exists: " +targetCover.getAbsolutePath() +". No update.\n");
            return;
        }

        FileOutputStream fos = new FileOutputStream(targetCover);
        fos.write(coverbyte);
        fos.close();
    }

    private void copyMetaDataIntoTargetDir(File f, File ftargetBook)
    {

    }

    private void copyFileIntoTargetDir(File fSource, File ftargetDir)
            throws IOException
    {
        File newTargetFile = new File(ftargetDir.getAbsolutePath() +File.separator +fSource.getName());
        Path pathSource = fSource.toPath();
        Path pathTarget = newTargetFile.toPath();
        Files.copy(pathSource, pathTarget, REPLACE_EXISTING,  COPY_ATTRIBUTES);
        FileTime creationTime  = (FileTime) Files.readAttributes(pathSource, "creationTime").get("creationTime");
        Files.setAttribute(pathTarget, "creationTime", creationTime);
        FileTime modTime  = Files.readAttributes(pathSource, BasicFileAttributes.class).lastModifiedTime();
        Files.setLastModifiedTime(pathTarget, modTime);
    }

    private File getImageFile(File [] allsubdirs)
    {
        return getFileOf("jpg", allsubdirs);
    }

    private File getMetadataFile(File [] allsubdirs)
    {
        return getFileOf("opf", allsubdirs);
    }

    private File getFileOf(String endsWith, File [] allsubdirs)
    {
        if (allsubdirs == null || allsubdirs.length == 0)
            return null;
        if (endsWith == null || allsubdirs.toString().trim().length() == 0)
            return null;
        for (File f : allsubdirs)
            if (f.getName().endsWith("." +endsWith))
                return f;
        return null;
    }

    private void copySelectedBookIntoDevice(File [] copyBookFiles)
    {
        if (copyBookFiles == null || copyBookFiles.length == 0) {
            laberMessage.setText("No ebooks to copy");
            return;
        }

        File fTargetDir = new File(textFieldTargetPath.getText());
        if (!fTargetDir.exists())
        {
            laberMessage.setText("No targetdir does not exists!");
            return;
        }

        File fTargetAuthor = null;
        File ftargetBookDir = null;
        for (File f : copyBookFiles)
        {
            fTargetAuthor = new File(fTargetDir.getAbsoluteFile() +File.separator +getAuthDirName(f));
            if (!fTargetAuthor.exists())
                if (!fTargetAuthor.mkdir())
                {
                    sbError.append("Cannot create none existing author dir: " +fTargetAuthor.getAbsolutePath() +"\n");
                    continue;
                }
            ftargetBookDir = new File(fTargetAuthor.getAbsoluteFile() +File.separator +getBookDirName(f));
            if (!ftargetBookDir.exists())
                if (!ftargetBookDir.mkdir())
                {
                    sbError.append("Cannot create none existing book dir: " +ftargetBookDir.getAbsolutePath() +"\n");
                    continue;
                }
            try {
                writeBookFilesIntoTarget(ftargetBookDir, f);
            }catch (IOException ioe){
                sbError.append("cannot write " +f.getAbsolutePath() +" into " +ftargetBookDir.getAbsolutePath() +"\n");
                continue;
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laberMessage.setText("Ebooks copy done"); // ddd
            }
        });
    }

    private File [] getSelectWhichBooksWillCopyed()
    {
        /* ddd
        // remove duplicates books: by example pdf and epub books with same title:
        for(String title : hmSourceBooks.keySet()) {

        }
        */

        // list books to copy where are not in target dir:
        File [] ret = null;
        List<File> listCopyFile = new ArrayList<File>();
        FoundedHashFiles foundedHashItem, sourceHashItem;
        boolean bTargetConstaisTitle, bCointansEpubBook;
        File epubFile = null, epubTargetFile;
        for(String title : hmSourceBooks.keySet())
        {
            bCointansEpubBook = false;
            epubFile = null;
            epubTargetFile = null;
            if (title != null && title.contains("Spring"))
            {
                System.out.println("finded!");
            }
            sourceHashItem = hmSourceBooks.get(title);
            if (sourceHashItem == null || sourceHashItem.arrFiles == null || sourceHashItem.arrFiles.length == 0)
                continue;
            bTargetConstaisTitle = hmTargetBooks.containsKey(title);
            if (bTargetConstaisTitle)
            {
                foundedHashItem = hmTargetBooks.get(title);
                if (foundedHashItem != null) {
                    epubTargetFile = getFileOf("epub", foundedHashItem.arrFiles);
                    if (epubTargetFile == null) {
                        String strName = sourceHashItem.arrFiles[0].getName();
                        int ind = strName.lastIndexOf('.');
                        String strExt = "pdf";
                        if (ind > -1)
                            strExt = strName.substring(ind +1);
                        epubTargetFile = getFileOf(strExt, foundedHashItem.arrFiles);
                    }
                    if (epubTargetFile != null) {
                        bCointansEpubBook = true;
                        continue;
                    }
                }
            }

            epubFile = getFileOf("epub", sourceHashItem.arrFiles);
            if (epubFile != null)
                listCopyFile.add(epubFile);
            else
                for(File f : sourceHashItem.arrFiles)
                {
                    if (f == null)
                        continue;
                    listCopyFile.add(f);
                }
        }

        ret = new File[listCopyFile.size()];
        ret = listCopyFile.toArray(ret);
        if (ret.length == 0)
            return null;
        return ret;
    }

    private void seekCompareDirBooks(File [] arrCompareDirs)
            throws IOException
    {
        if (arrCompareDirs == null || arrCompareDirs.length == 0)
            return;
        File [] ebooks, subdirs;
        String title;
        for(File dir : arrCompareDirs) {
            ebooks = dir.listFiles(ebookfilter);
            // addEBooksIntoHash(ebooks);
            if (ebooks != null && ebooks.length > 0)
                hmCompareBooks = updateFilesWithThisHash(ebooks, hmCompareBooks);
            subdirs = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            seekCompareDirBooks(subdirs);
        }
    }

    private void seekSourceDirBooks(File fSourceDir)
            throws IOException
    {
        if (fSourceDir == null || !fSourceDir.exists())
            return;
        File [] ebooks = fSourceDir.listFiles(ebookfilter);
        // addEBooksIntoHash(ebooks);
        // addEBooksIntoHash(ebooks);
        if (ebooks != null && ebooks.length > 0)
            hmSourceBooks = updateFilesWithThisHash(ebooks, hmSourceBooks);
        File [] subdirs = fSourceDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        for(File dir : subdirs)
        {
            seekSourceDirBooks(dir);
        }
        // addDirsIntoEBookHash(subdirs);
    }

    private String getStrikeChars()
    {
        if (iStrikeChars > 30)
            iStrikeChars = 0;
        int max = ++iStrikeChars;
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i < max; i++)
            sb.append(".");
        return sb.toString();
    }

    public void updateMessage(String msg)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                laberMessage.setText(msg);
       }});
    }

    private FoundedHashFiles getSimilarTitle(String title, HashMap<String,FoundedHashFiles> hmDir)
    {
        FoundedHashFiles ret = null;
        if (title == null || title.trim().length() == 9)
            return null;
        double dSimilary = 0.0;
        for(String key : hmDir.keySet())
        {
            dSimilary = simalarity.apply(title, key);
            if (dSimilary > dSimilar) {
                ret = hmDir.get(key);
                break;
            }
        }
        return ret;
    }

    private FoundedHashFiles getStartWith(String title, HashMap<String,FoundedHashFiles> hmDir)
    {
        StartWithimportHashMap hashMap = new StartWithimportHashMap(hmDir);
        return hashMap.getStartWith(title);
    }

    private HashMap<String,FoundedHashFiles>
    updateFilesWithThisHash(File [] ebooks, HashMap<String,FoundedHashFiles> hmDir)
            // throws IOException
    {
        String title;
        FoundedHashFiles fhashfiles;
        hashMap = new StartWithimportHashMap(hmDir);
        for (File f : ebooks)
        {
            try {
                /*
                if (f.getName().contains("JBoss in Action"))
                {
                    System.out.println("finded!");
                }
                 */

                if (checkBoxOnlyEpubs.isSelected() && !f.getName().endsWith(".epub"))
                    continue;

                title = getTitle(f);
                fhashfiles = (FoundedHashFiles) hmDir.get(title);
                if (fhashfiles == null && checkBoxStartWith.isSelected())
                    fhashfiles = hashMap.getStartWith(title);
                    // fhashfiles = getStartWith(title, hmDir);
                if (fhashfiles == null && checkBoxSimilar.isSelected())
                    fhashfiles = getSimilarTitle(title, hmDir);

                if (fhashfiles == null) {
                    fhashfiles = new FoundedHashFiles();
                    fhashfiles.title = title;
                    fhashfiles.arrFiles = new File[1];
                    fhashfiles.arrFiles[0] = f;
                } else {
                    File[] arrFiles = fhashfiles.arrFiles;
                    int max = (fhashfiles.arrFiles == null ? 0 : fhashfiles.arrFiles.length);
                    fhashfiles.arrFiles = new File[max + 1];
                    int i = 0;
                    for (File f2 : arrFiles) {
                        fhashfiles.arrFiles[i++] = f2;
                    }
                    fhashfiles.arrFiles[i] = f;
                }
                hmDir.put(title, fhashfiles);
                updateMessage(getStrikeChars());
            }catch (IOException ioe){
                sbError.append("Error: " + f.getAbsolutePath() +" " +ioe.getMessage());
                System.err.println("" +f.getAbsolutePath());
               // ioe.printStackTrace();
            }
        }
        return hmDir;
    }

    private void seekSortedTargetDirBooks(File fTargetDir)
    // throws IOException
    {
        if (fTargetDir == null || !fTargetDir.exists())
            return;
        File [] ebooks = fTargetDir.listFiles(ebookfilter);
        if (ebooks != null && ebooks.length > 0) {
            for(File f : ebooks) {
                if (checkBoxOnlyEpubs.isSelected() && !f.getName().endsWith(".epub"))
                    continue;
                fileList.add(f);
            }
        }

        File [] subdirs = fTargetDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        for(File dir : subdirs)
        {
            seekSortedTargetDirBooks(dir);
        }
    }

    private void seekMissingTargetDirBooks(File fTargetDir, boolean bRootLevel)
    // throws IOException
    {
        if (fTargetDir == null || !fTargetDir.exists())
            return;
        File [] files = fTargetDir.listFiles();
        File [] ebooks = fTargetDir.listFiles(ebookfilterAll);
        File [] subdirs = fTargetDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        /*
        if ((subdirs == null || subdirs.length == 0) && ebooks.length == 0)
        {
            System.out.println("kissa");
        }
         */
        if (!bRootLevel && (ebooks == null || ebooks.length == 0) && (subdirs == null || subdirs.length == 0))
            dirsMissingTitle.add(fTargetDir);
        for(File dir : subdirs)
        {
            seekMissingTargetDirBooks(dir, false);
        }
    }

    private void seekTargetDirBooks(File fTargetDir)
           // throws IOException
    {
        if (fTargetDir == null || !fTargetDir.exists())
            return;
        File [] ebooks = fTargetDir.listFiles(ebookfilter);
        if (ebooks != null && ebooks.length > 0)
            hmTargetBooks = updateFilesWithThisHash(ebooks, hmTargetBooks);
        // addEBooksIntoHash(ebooks);
        File [] subdirs = fTargetDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        for(File dir : subdirs)
        {
            seekTargetDirBooks(dir);
        }
    }

    private void addDirsIntoEBookHash(File [] subdirs)
    {
        if (subdirs == null || subdirs.length == 0)
            return;
        for(File dir : subdirs)
        {
            File [] ebooks = dir.listFiles(ebookfilter);
            addEBooksIntoHash(ebooks);
            File [] dirs = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            addDirsIntoEBookHash(dirs);
        }
    }

    private void addEBooksIntoHash(File [] ebooks)
    {

    }

    private String getToolTipTitle(File fDocument)
        //    throws IOException //,,FileNotFoundException, SAXException, TikaException
    {
        String title = "";
        if (fDocument.getName().endsWith(".pdf")) {
            String tile = null;
            PDDocument doc = null;
            try {
                doc = PDDocument.load(fDocument);
                PDDocumentInformation info = doc.getDocumentInformation();
                // PDDocumentCatalog catalog = doc.getDocumentCatalog();
                // PDMetadata metadata = catalog.getMetadata();
                // To read the XML metadata
                //InputStream xmlInputStream = metadata.createInputStream();
                tile = info.getTitle();
                doc.close();
            }catch (InvalidPasswordException ipe){
                System.err.println("getTitle pdf InvalidPasswordException= " +fDocument.getAbsolutePath());
                tile = null;
            }catch (IOException ipe){
                System.err.println("getTitle pdf io exeception= " +fDocument.getAbsolutePath());
                tile = null;
            }
            finally {
                if (doc != null)
                    try {
                        doc.close();
                    }catch (Exception e){
                    }
            }
            return tile;
        }
        else
        {
            if (fDocument.getName().endsWith(".epub")) {
                try {
                    String xmltitle = getEpubTitle(fDocument);
                    if (xmltitle != null)
                        title = xmltitle.trim();
                }catch (IOException ioe){
                    title = null;
                }
            }
        }
        if (bDebug)
            System.err.println("epub getTitle value=" +title);

        return title;
        /*
        //Parser method parameters
        Parser parser = new PDFParser();
        BodyContentHandler handler = new BodyContentHandler();
        // ContentHandler handler = new WriteOutContentHandler(0);
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(fDocument);
        ParseContext context = new ParseContext();

        parser.parse(inputstream, handler, metadata, context);
        System.out.println(handler.toString());

        //getting the list of all meta data elements
        /*
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }
         */
        // return metadata.get(TikaCoreProperties.TITLE);
    }

    private String getTitle(File fDocument)
            throws IOException //,,FileNotFoundException, SAXException, TikaException
    {
        String title = "";
        if (fDocument.getName().endsWith(".pdf")) {
            String tile = null;
            PDDocument doc = null;
            try {
                doc = PDDocument.load(fDocument);
                PDDocumentInformation info = doc.getDocumentInformation();
                // PDDocumentCatalog catalog = doc.getDocumentCatalog();
                // PDMetadata metadata = catalog.getMetadata();
                // To read the XML metadata
                //InputStream xmlInputStream = metadata.createInputStream();
                tile = info.getTitle();
                if (tile == null || tile.trim().length() == 0) {
                    if (bDebug)
                        System.err.println("getTitle pdf value=" + tile +" " +fDocument.getAbsolutePath());
                    tile = fDocument.getName().replace(".pdf", "");
                    int ind = tile.indexOf('-');
                    if (ind > -1)
                        tile = tile.substring(0,ind).trim();
                    String strAbsolutePath = fDocument.getAbsolutePath();
                    String strSplit = "/";
                    if (File.separatorChar == '\\')
                        strSplit = "\\\\";
                    String [] arrPaths = strAbsolutePath.split(strSplit);
                    String pathTitle = arrPaths[arrPaths.length-2];
                    String pathAuthor = arrPaths[arrPaths.length-3];
                    tile = pathTitle;
                    int indNumberStart = pathTitle.indexOf('(');
                    int indNumberEnd = pathTitle.indexOf(')');
                    if (indNumberStart > -1 && indNumberEnd > -1 )
                    {
                        tile = pathTitle.substring(0, indNumberStart);
                    }
                    final String cnstSearch = "[eBook]";
                    if (tile.contains(cnstSearch))
                    {
                        int ind2 = tile.indexOf(cnstSearch);
                        if (ind2 > -1)
                        {
                            tile = tile.substring(0, ind2);
                        }
                    }
                    if (tile.contains(pathAuthor))
                        tile = tile.replace(pathAuthor, "");
                    int indStrike = tile.lastIndexOf('-');
                    if (indStrike > -1)
                        tile = tile.replace("-",""); // tile.substring(0, indStrike);
                    // tile = tile.replace(cnstSearch, "").trim();
                    tile = tile.trim();
                } else
                if (bDebug)
                    System.err.println("getTitle pdf value=" + tile);
            }catch (InvalidPasswordException ipe){
                System.err.println("getTitle pdf InvalidPasswordException= " +fDocument.getAbsolutePath());
                tile = fDocument.getName().replace(".pdf", "");
            }
            finally {
                if (doc != null)
                    doc.close();
            }
            return tile;
        }
        else
        {
            int ind = fDocument.getName().lastIndexOf("_");
            if (ind == -1)
                ind = fDocument.getName().lastIndexOf("-");
            if (ind == -1)
                title = fDocument.getName();
            else
            {
                title = fDocument.getName().substring(0, ind);
                if (title != null)
                    title = title.trim();
            }
            if (fDocument.getName().endsWith(".epub")) {
                String xmltitle = getEpubTitle(fDocument);
                if (xmltitle != null)
                    title = xmltitle.trim();
            }
        }
        if (title == null || title.trim().length() == 0)
        {
            if (bDebug)
                System.err.println("getTitle epub value=" +title);
            title = fDocument.getName().replace(".epub","");
        }
        else
        if (bDebug)
            System.err.println("epub getTitle value=" +title);

        return title;
        /*
        //Parser method parameters
        Parser parser = new PDFParser();
        BodyContentHandler handler = new BodyContentHandler();
        // ContentHandler handler = new WriteOutContentHandler(0);
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(fDocument);
        ParseContext context = new ParseContext();

        parser.parse(inputstream, handler, metadata, context);
        System.out.println(handler.toString());

        //getting the list of all meta data elements
        /*
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }
         */
        // return metadata.get(TikaCoreProperties.TITLE);
    }


    private String getEpubTitle(File fDocument)
            throws IOException
    {
        byte[] buffer = new byte[1024];

        String ret = null;
        if (fDocument == null || !fDocument.exists())
            return null;
        // dddd
        final StringBuffer sb = new StringBuffer();
        String dirPath = "META-INF/container.xml";
        ZipFile zipFile = new ZipFile(fDocument);
        //Iterable<ZipArchiveEntry> entries = zipFile.getEntries(dirPath);
        ZipArchiveEntry container = zipFile.getEntry(dirPath);
        InputStream in = zipFile.getInputStream(container);

        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        br.lines().forEach(line -> sb.append(line +"\n"));
        String data = sb.toString();
        in.close();
        int ind = data.indexOf("<rootfile");
        if (ind > -1)
        {
            data = data.substring(ind);
            final String var = "full-path";
            ind = data.indexOf(var);
            if (ind > -1) {
                data = data.substring(ind +var.length());
                ind = data.indexOf("=");
                if (ind > -1)
                    data = data.substring(ind +1);

                ind = data.indexOf('"');
                if (ind > -1)
                    data = data.substring(ind +1);
                ind = data.indexOf('"');
                if (ind > -1)
                    data = data.substring(0, ind);

                String metadatafname = data;
                if (metadatafname != null && metadatafname.trim().length() > 0) {
                    ZipArchiveEntry container2 = zipFile.getEntry(metadatafname);
                    InputStream in2 = zipFile.getInputStream(container2);
                    final StringBuffer sb2 = new StringBuffer();
                    InputStreamReader isr2 = new InputStreamReader(in2, StandardCharsets.UTF_8);
                    br = new BufferedReader(isr2);
                    br.lines().forEach(line -> sb2.append(line + "\n"));
                    data = sb2.toString();
                    int ind2 = data.indexOf("<dc:title");
                    if (ind2 > -1) {
                        data = data.substring(ind2);
                        ind2 = data.indexOf(">");
                        if (ind2 > -1)
                            data = data.substring(ind2+1);
                        ind2 = data.indexOf("</dc:title>");
                        if (ind2 > -1)
                            data = data.substring(0, ind2);
                        ret = data;
                    }
                }
            }
        }
        zipFile.close();
        return ret;
    }


    private String getEpubCoverFileName(File fDocument)
            throws IOException {

        String ret = null;
        if (fDocument == null || !fDocument.exists() || !fDocument.getName().endsWith(".epub"))
            return null;
        final StringBuffer sb = new StringBuffer();
        String dirPath = "META-INF/container.xml";
        ZipFile zipFile = new ZipFile(fDocument);
        //Iterable<ZipArchiveEntry> entries = zipFile.getEntries(dirPath);
        ZipArchiveEntry container = zipFile.getEntry(dirPath);
        InputStream in = zipFile.getInputStream(container);

        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        br.lines().forEach(line -> sb.append(line + "\n"));
        String data = sb.toString();
        in.close();
        zipFile.close();
        if (data == null || data.length() == 0)
            return null;

        Pattern p = Pattern.compile("<meta\\s+name\\s*=\\s*\"cover\"\\s+content\\s*=\\s*\"(.*?)\"\\s*/>");
        Matcher m = p.matcher(data);
        if (m.find()) {
            String coverpropertyname = m.group();
            if (coverpropertyname == null || coverpropertyname.trim().length() == 0)
                return null;
            Pattern p2 = Pattern.compile("<item\\s+id=\"cover-image\".*?/>".replace("cover-image", coverpropertyname));
            Matcher m2 = p2.matcher(data);
            if (m2.find()) {
                String strXml = m2.group();
                Pattern p3 = Pattern.compile("href=\"(.*?)\"");
                Matcher m3 = p3.matcher(strXml);
                if (m3.find()) {
                    String strImageName = m3.group();
                    if (strImageName == null || strImageName.trim().length() == 0)
                        return null;
                    String strImageFileName = "OEBPS/" + strImageName;
                    return strImageFileName;
                }
            }
        }
        return null;
    }

    private byte [] getEpubCover(File fDocument)
            throws IOException
    {
        String ret = null;
        if (fDocument == null || !fDocument.exists())
            return null;
        String strImageName = getEpubCoverFileName(fDocument);
        if (strImageName == null || strImageName.trim().length() == 0)
            return null;
        String strImageFileName = "OEBPS/" + strImageName;
        ZipFile zipFile = new ZipFile(fDocument);
        ZipArchiveEntry imageEntry = zipFile.getEntry(strImageFileName);
        InputStream in2 = zipFile.getInputStream(imageEntry);
        InputStreamReader isr2 = new InputStreamReader(in2);
        BufferedReader br = new BufferedReader(isr2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i = 0;
        while ((i = br.read()) != -1)
            out.write(i);
        zipFile.close();
        return out.toByteArray();
    }

    private String entryName(File f)
    {
        return f.getName();
    }

    private void compressDirectory(File fDir)
            throws IOException
    {
        File [] filesToArchive = fDir.listFiles();
        for (File f : filesToArchive) {
            // maybe skip directories for formats like AR that don't store directories
            ArchiveEntry entry = compressOStream.createArchiveEntry(f, entryName(f));
            // potentially add more flags to entry
            compressOStream.putArchiveEntry(entry);
            if (f.isFile()) {
                //                  compressOStream.putArchiveEntry(entry);
                try (InputStream i = Files.newInputStream(f.toPath())) {
                    IOUtils.copy(i, compressOStream);
                }
//                compressOStream.closeArchiveEntry();
            }
            compressOStream.closeArchiveEntry();
            if (f.isDirectory())
                compressDirectory(f);
           // compressOStream.closeArchiveEntry();
        }
    }

    private void addFileToCompress(ArchiveOutputStream tOut, String path, String base)
            throws IOException
    {
        File f = new File(path);
        System.out.println(f.exists());
        ListViewDirItem item = new ListViewDirItem();
        item.showname = "doc " + f.getAbsolutePath() + " ...";
        item.path = f.getAbsolutePath();
        item.title = getToolTipTitle(f);
        listitems.add(item);
        String entryName = base + f.getName();
        ArchiveEntry tarEntry = compressOStream.createArchiveEntry(f, entryName);
        tOut.putArchiveEntry(tarEntry);

        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    System.out.println(child.getName());
                    addFileToCompress(tOut, child.getAbsolutePath(), entryName + "/");
                    item = new ListViewDirItem();
                    item.showname = child.getAbsolutePath();
                    item.path = child.getAbsolutePath();
                    item.title = getToolTipTitle(child);
                    updateMessage(getStrikeChars());
                    listitems.add(item);
                }
            }
        }
    }

    private void startFileCompress(File fSourceDir, File fTargetDir)
    {
        // Take a buffer
        byte[] buffer = new byte[1024];

        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelList.setText("Compress seleccted source dir into target dir.");
                }});

         //  if (labelList.getText().trim().length() == 0){

            final String tmpfend = "4343434";
            String strCompressFilePath = fTargetDir.getAbsolutePath()
                    + File.separator + fSourceDir.getName() + "." + selectedCompres;
            String strTmpCompressFilePath = fTargetDir.getAbsolutePath()
                    + File.separator + fSourceDir.getName() + tmpfend + "." + "zip";

            File compressFile = new File(strCompressFilePath);
            if (selectedCompres.equals("zip"))
                compressOStream = new ZipArchiveOutputStream(compressFile);
            else if (selectedCompres.equals("tar.gz")) {
                OutputStream fo = Files.newOutputStream(Paths.get(compressFile.getAbsolutePath()));
                OutputStream gzo = new GzipCompressorOutputStream(fo);
                // ArchiveOutputStream o = new TarArchiveOutputStream(gzo);
                compressOStream = new TarArchiveOutputStream(gzo);
            } else if (selectedCompres.equals("tar")) {
                OutputStream fo = Files.newOutputStream(Paths.get(compressFile.getAbsolutePath()));
                // ArchiveOutputStream o = new TarArchiveOutputStream(gzo);
                compressOStream = new TarArchiveOutputStream(fo);
            } else {
                buttonExecute.setDisable(false);
                return;
            }

            String dirPath = fSourceDir.getAbsolutePath();
             // tarGzPath = "archive.tar.gz";
            // fOut = new FileOutputStream(compressFile);
            addFileToCompress(compressOStream, dirPath, "");
            compressOStream.finish();
            compressOStream.close();

            // compressDirectory(fSourceDir);
            /*
            TFile t_fSourceDir = new TFile(fSourceDir);

            /*
            TFile t_fSourceDir = new TFile(fSourceDir);

            Files.walkFileTree(fSourceDir, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    Files.copy(file, targetFile.resolve(t_fSourceDir.relativize(file)),
                            StandardCopyOption.COPY_ATTRIBUTES);
                    return super.visitFile(file, attrs);
                }
            });
            */

            /*
            File f = new File(strTmpCompressFilePath);
            if (f.exists())
                f.delete();
            TFile zipfile = new TFile(strTmpCompressFilePath);
            if (?* selectedCompres.equals("zip") && *? t_fSourceDir.isDirectory())
            {
                zipfile = new TFile(strCompressFilePath);
                f = new File(strCompressFilePath);
                zipfile = new TFile(zipfile, t_fSourceDir.getName());
            }

            t_fSourceDir.cp_rp(zipfile);
            if (selectedCompres.equals("zip") && f.exists())
            {
                ;
                ?*
                File fzip = new File(strCompressFilePath);
                if (fzip.exists())
                    fzip.delete();
                File zipf = new File(strCompressFilePath);
                File zip2file = new File(strTmpCompressFilePath);
                zip2file.renameTo(zipf);
                 *?
            }
            else {
                File fzip = new File(strCompressFilePath);
                if (fzip.exists())
                    fzip.delete();
                TFile tarfile = new TFile(strCompressFilePath);
                TFile zip2file = new TFile(strTmpCompressFilePath);
                zip2file.cp_rp(tarfile);
            }
            */
            /*
            File [] childFiles = fSourceDir.listFiles();
            TFile src, dst;

            for (File f : childFiles) {
                /*
                src = new TFile(f); // e.g. "file"
                dst = new TFile(strCompressFilePath); // e.g. "archive.zip"
                if (src.isDirectory())
                    dst = new TFile(dst, src.getName());
                src.cp_rp(dst);
                 */

                /*
                Files.walkFileTree(f, new SimpleFileVisitor<Path>()
                {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                            throws IOException
                    {
                        Files.copy(f, targetFile.resolve(f.relativize(file)),
                                StandardCopyOption.COPY_ATTRIBUTES);
                        return super.visitFile(file, attrs);
                    }
                });
            }
                 */
          /*
          // Create object of FileOutputStream
          FileOutputStream fos = new FileOutputStream(strCompressFilePath);
          // Get ZipOutstreamObject Object
          ZipOutputStream zos = new ZipOutputStream(fos);

          ZipEntry ze = new ZipEntry("source.dat");
          zos.putNextEntry(ze);
          FileInputStream in = new FileInputStream("C:\\source.dat");
          int len;
          while ((len = in .read(buffer)) > 0) {
             zos.write(buffer, 0, len);
          }
          in .close();
          zos.closeEntry();

          //remember close it
          zos.close();
            */
      //      System.out.println("Done");
     //       }
        } catch (Exception ex) {
            ex.printStackTrace();
                    strMessae = ex.getMessage();
        }
    }

    private void startFileCopying(File fSourceDir, File p_fTargetDir)
            throws IOException
    {
        File fTargetDir = p_fTargetDir;
        File [] arrSourceChildren = fSourceDir.listFiles();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textFieldCurrentDir.setText(fSourceDir.getAbsolutePath());
            }});
        arrTargetChildren = fTargetDir.listFiles();
        for(File fSource : arrSourceChildren)
        {
            if (skipSourceFile(fSource, fTargetDir))
                continue;
            try {
                copySourceFileIntoTargetDir(fSource, fTargetDir);
            }catch (IOException ioe){
                ioe.printStackTrace();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        laberMessage.setStyle("-fx-background-color: red");
                        sbError.append("" + fSource.getAbsolutePath() + ": " + ioe.getMessage() + "\n");
                        laberMessage.setText("Error occurred in copying. Press Show Error button.");
                    }});
                throw  ioe;
            }
        }

        File newTargetDir;
        for(File fSource : arrSourceChildren)
        {
            if (fSource.isFile())
                continue;
            newTargetDir = new File(fTargetDir.getAbsolutePath() +File.separator +fSource.getName());
            copySourceDirIntoTargetDir(fSource, newTargetDir);
        }
    }

    private boolean copySourceDirIntoTargetDir(File fSource, File fTargetDir)
            throws IOException
    {
        if (fSource == null)
        {

            laberMessage.setText("Source dir is null:");
            return false;
        }
        if (!fSource.exists())
        {

            laberMessage.setText("Source dir does not exists: " +fSource.getAbsolutePath());
            return false;
        }
        if (fSource.isFile())
        {
            laberMessage.setText("Source dir is a file: " +fSource.getAbsolutePath());
            return false;
        }

        if (fTargetDir == null || fTargetDir.isFile())
        {
            laberMessage.setText("Target dir is a file: " +fTargetDir.getAbsolutePath());
            return false;
        }
        if (!fTargetDir.exists())
            if (!fTargetDir.mkdir()) {
                laberMessage.setText("Cannot create a dir: " +fTargetDir.getAbsolutePath());
                return false;
         }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textFieldCurrentDir.setText(fSource.getAbsolutePath());
            }});

        startFileCopying(fSource, fTargetDir);
        return true;
    }

    private boolean copySourceFileIntoTargetDir(File fSource, File fTargetDir)
            throws IOException
    {
        boolean ret = false;
        File newTargetFile = new File(fTargetDir.getAbsolutePath() +File.separator +fSource.getName());
        Path pathSource = fSource.toPath();
        Path pathTarget = newTargetFile.toPath();
        Files.copy(pathSource, pathTarget, REPLACE_EXISTING,  COPY_ATTRIBUTES);
        FileTime creationTime  = (FileTime) Files.readAttributes(pathSource, "creationTime").get("creationTime");
        Files.setAttribute(pathTarget, "creationTime", creationTime);
        FileTime modTime  = Files.readAttributes(pathSource, BasicFileAttributes.class).lastModifiedTime();
        Files.setLastModifiedTime(pathTarget, modTime);
        ListViewDirItem item = new ListViewDirItem();
        item.showname = fSource.getAbsolutePath();
        item.path = item.showname;
        item.title = getToolTipTitle(fSource);
        listitems.add(item);
        updateMessage(getStrikeChars());
        return true;
    }

    private boolean skipSourceFile(File fSource, File fTargetDir)
    {
        boolean ret = false;
        if (fSource == null)
        {
            laberMessage.setText("Source file is null");
            return true;
        }
        if (!fSource.exists())
        {
            String msg = "Source file does not exists: " +fSource.getAbsolutePath();
            laberMessage.setText(msg);
            sbError.append(msg +'\n');
            return true;
        }
        if (fSource.isDirectory())
        {
            /*
            String msg = "Source file is a dir: " +fSource.getAbsolutePath();
            laberMessage.setText(msg);
            sbError.append(msg +'\n');
             */
            return true;
        }
        if (!fSource.canRead())
        {
            String msg = "Source file cannot read: " +fSource.getAbsolutePath();
            sbError.append(msg +'\n');
            return true;
        }

        File newTargetFile = new File(fTargetDir.getAbsolutePath() +File.separator +fSource.getName());
        if (newTargetFile == null || !newTargetFile.exists())
            return false;
        if (newTargetFile.isDirectory())
            return true;
        if (newTargetFile.length() != fSource.length())
            return false;
        if (newTargetFile.lastModified() != fSource.lastModified())
               return false;
        return true;
    }

    public void buttonSourcePressed()
    {
//        System.out.println("buttonSourcePressed");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select source dir");
        File defaultDirectory = new File(getRootDir());
        if (textFieldSourcePath.getText().trim().length()>0)
            defaultDirectory = new File(textFieldSourcePath.getText());

        if (defaultDirectory.exists())
            directoryChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = directoryChooser.showDialog(this.primaryStage);
        if (selectedDirectory != null && selectedDirectory.exists())
        {
            textFieldSourcePath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void buttonTargetPressed()
    {
        System.out.println("buttonTargetPresed");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("SeLect target dir");
        File defaultDirectory = new File(getRootDir());
        if (textFieldTargetPath.getText().trim().length()>0)
            defaultDirectory = new File(textFieldTargetPath.getText());

        if (defaultDirectory.exists())
            directoryChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = directoryChooser.showDialog(this.primaryStage);
        if (selectedDirectory != null && selectedDirectory.exists())
        {
            textFieldTargetPath.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
