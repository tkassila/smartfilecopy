package smartfilecopy;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.layout.BorderPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

public class Main extends Application {

    private Stage m_primaryStage;
    private Controller controller = new Controller();
    public Controller getController()
    {
        return controller;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("smartfilecopy.fxml"));
        fxmlLoader.setController(controller);
        controller.setPrimaryStage(primaryStage);
        Parent loadedroot = fxmlLoader.load();
        // loadedroot.setStyle("-fx-background-color: yellow");
        // Get current screen of the stage

        Scene scene = new Scene(loadedroot, 1000, 800 );
       // scene.setX(bounds.getMinX());
      //  scene.setY(bounds.getMinY());
//        scene.setWidth(bounds.getWidth());
  //      scene.setHeight(bounds.getHeight());


        GridPane gp = (GridPane)loadedroot;
        // gp.minWidthProperty().bind(primaryStage.heightProperty().multiply(1.5));
        // gp.minHeightProperty().bind(primaryStage.widthProperty().divide(1.5));
        VBox.setVgrow(gp, Priority.ALWAYS);
        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
             //  gp.setWidth(newSceneWidth.doubleValue());
                System.out.println(" gp Width: " + gp.getWidth());
                //gp.setPrefWidth(newSceneWidth.doubleValue());
                controller.getVboxMain().setPrefWidth(newSceneWidth.doubleValue());
            }
        });
        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
         //       gp.setHeight(newSceneHeight.doubleValue());
                System.out.println(" gp Height: " + gp.getHeight());
                // gp.setPrefHeight(newSceneHeight.doubleValue());
                controller.getVboxMain().setPrefHeight(newSceneHeight.doubleValue());
            }
        });

        /*
        GridPane gp = (GridPane)loadedroot;
        gp.prefWidthProperty().bind(scene.widthProperty());
        gp.prefHeightProperty().bind(scene.heightProperty());
         */
        primaryStage.setTitle("Smart File Copy: copying files/dirs, compress dir and it's files, copy ebooks from calibre dir, find tuple target files for one title");
        primaryStage.setScene(scene);
        m_primaryStage = primaryStage;
        m_primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                controller.appIsClosing();
            }
        });

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(700);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
