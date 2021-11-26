
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dictionary_V2 extends Application {
    private static final double WIDTH = 600;
    private static final double LEFT_PANE_WIDTH = 200;  // initial width of left pane
    private static final String PRIMARY_COLOR = "#191970";
    private static final String SECONDARY_COLOR = "#C3CEDA";

    private final Image ICON = new Image( "icon.jpg");
    private final String DICT_FILE = "dict.txt";

    private BorderPane rootPane;

    // top part
    private Label topLabel;

    // left part
    private TextField searchTextField;
    private ListView<String> wordsListview;

    // center part
     private Label wordLabel;
    private Label definitionLabel;
    private CheckBox starredCheckBox;

    // data
    private ArrayList<String> wordsList;
    private HashMap<String, String> dictMap;
    private HashSet<String> starredWordsSet;

    // menus
    private MenuBar menuBar;

    private Menu fileMenu;
    private Menu aboutMenu;

    private CheckMenuItem seeStarredMenuItem;
    private MenuItem aboutMenuItem;
    private VBox vBox;
    private HashMap<String,Boolean> isStarred;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadDict();
        initLayout();
        initHandlers();
        primaryStage.setIconified(true);
        primaryStage.getIcons().add(ICON);
        primaryStage.setTitle("GraulKz");



        primaryStage.setScene(new Scene(rootPane,WIDTH,600));
        primaryStage.show();



    }

    public void initLayout() {


     menuBar = new MenuBar();
     fileMenu= new Menu("File");
     seeStarredMenuItem=new CheckMenuItem("starred words");
     fileMenu.getItems().add(seeStarredMenuItem);
     aboutMenu=new Menu("about");
     aboutMenuItem= new MenuItem("About this program");
     aboutMenu.getItems().add(aboutMenuItem);
     menuBar.getMenus().addAll(fileMenu,aboutMenu);

//     Rectangle rectangle = new Rectangle(600,100);
//     rectangle.setStyle("-fx-background-color: "+PRIMARY_COLOR);

     topLabel=new Label("English Dictionary");
   //  topLabel.setContentDisplay(ContentDisplay.CENTER);
     topLabel.setAlignment(Pos.CENTER);
     topLabel.setFont(Font.font("Impact",40));
     topLabel.setTextFill(Color.WHITE);
     topLabel.setLayoutX(200);
     topLabel.setLayoutY(45);
     wordsListview=new ListView<>();

      vBox = new VBox(10);
     vBox.setMinWidth(LEFT_PANE_WIDTH);

     vBox.setStyle("-fx-background-color: "+SECONDARY_COLOR);
     rootPane= new BorderPane();
     Pane pane = new Pane(topLabel,menuBar);
     pane.setStyle("-fx-background-color: "+PRIMARY_COLOR);
     pane.setMinHeight(130);
     rootPane.setTop(pane);
     wordsListview.getItems().addAll(wordsList);
     searchTextField=new TextField("search...");

     vBox.getChildren().addAll(searchTextField,wordsListview);
     rootPane.setLeft(vBox);
     //-------

     wordLabel= new Label();
     definitionLabel=new Label();
     starredCheckBox= new CheckBox();
     wordLabel.setLayoutY(5);
     wordLabel.setLayoutX(5);
     definitionLabel.setLayoutY(50);
     definitionLabel.setLayoutX(5);

     wordLabel.setContentDisplay(ContentDisplay.RIGHT);


     Pane pane1 = new Pane(definitionLabel,wordLabel);
     rootPane.setCenter(pane1);
     starredWordsSet=new HashSet<>();
    }

    public void initHandlers() {
     rootPane.setOnMouseClicked(event -> {
      if(searchTextField.getText().equals(""))
      searchTextField.setText("search...");
     });
     searchTextField.setOnKeyPressed(event -> {
      search();
     });
     searchTextField.setOnMouseClicked(event -> {

      searchTextField.clear();

     });
     aboutMenuItem.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);

      String text ="This program was developed by Adill For more information ,please contact:@graulkz ";
      alert.setContentText(text);

      alert.setTitle("About this program");

      alert.show();
     });
     wordsListview.setOnMouseClicked(event -> {
       view();

     });
     seeStarredMenuItem.setOnAction(event -> {

           wordsListview.getItems().clear();
           if(seeStarredMenuItem.isSelected()) {
            wordsListview.getItems().addAll(starredWordsSet);
           }
           else {
             wordsListview.getItems().addAll(wordsList);
           }

     });
     starredCheckBox.setOnAction(event -> {
      if( starredCheckBox.isSelected()){
       starredWordsSet.add(wordLabel.getText());
       isStarred.put(wordLabel.getText(),true);
      }else {
        starredWordsSet.remove(wordLabel.getText());
        isStarred.put(wordLabel.getText(),false);
      }


     });

    }

 private void view() {
     String a = wordsListview.getSelectionModel().getSelectedItem();
     //System.out.println(a);

     wordLabel.setText(a);
     definitionLabel.setText(dictMap.get(a));
     wordLabel.setGraphic(starredCheckBox);
     starredCheckBox.setSelected(isStarred.get(a));
     definitionLabel.setWrapText(true);
     definitionLabel.setMaxWidth(350);
     Pane pane1 = new Pane(definitionLabel,wordLabel);
     rootPane.setCenter(pane1);



 }

 private void search() {
  Iterator iterator ;
   if(!seeStarredMenuItem.isSelected()) {
       iterator=wordsList.iterator();
   }else {
       iterator=starredWordsSet.iterator();
   }

    ArrayList<String> temp = new ArrayList<>();
    String field = searchTextField.getText();
     while (iterator.hasNext()){

      String a=(String)iterator.next();
     if (a.matches(field + ".*")) {
      temp.add(a);
     }
    }
    if (temp.size() == 0) {
     rootPane.getChildren().remove(1);
     TextArea area = new TextArea();
     area.setText("\n\n\n\n\n\n\n\n\n     no such words...");
     // area.setMinSize(80,500);
     area.setMaxWidth(200);
     area.setMinHeight(500);
     vBox.getChildren().clear();
     vBox.getChildren().addAll(searchTextField, area);
     rootPane.setLeft(vBox);

    } else {
     vBox.getChildren().clear();
     vBox.getChildren().addAll(searchTextField, wordsListview);
     rootPane.setLeft(vBox);
     wordsListview.getItems().clear();
     wordsListview.getItems().addAll(temp);
    }



 }

 public void loadDict() {
     isStarred= new HashMap<>();
     File file = new File(DICT_FILE);
     Scanner read=null;
     try {
      read = new Scanner(file);
     } catch (FileNotFoundException e) {}

      dictMap= new HashMap<>();
       wordsList = new ArrayList<>();
       while (read.hasNextLine()){
       read.nextLine();
       if(!read.hasNextLine()){
          break;
       }
        String a =read.nextLine();
        if(!read.hasNextLine()){
          break;
       }
        wordsList.add(a);
        dictMap.put(a,read.nextLine());
        isStarred.put(a,false);
        if(!read.hasNextLine()){
          break;
       }
        
        
        read.nextLine();
       }

    }
}