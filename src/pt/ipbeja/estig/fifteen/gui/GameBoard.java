package pt.ipbeja.estig.fifteen.gui;

import java.util.*;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import pt.ipbeja.estig.fifteen.model.*;

/**
 * The fifteen main view
 *
 * @author João Paulo Barros e Rui Pais
 * @version 2014/05/19 - 2016/04/03 - 2017/04/19 - 2019/05/06 - 2021/05/18
 */
public class GameBoard extends Application implements View {
    private final String ICON_FILE = "/resources/images/background100.png";
    private Model model;

    private Button startButton;
    private Pane pane;
    private Map<String, GameImage> movingImages;

    private Label timeLabel;
    private static Map<KeyCode, Direction> directionMap = new HashMap<>();

    static {
        directionMap.put(KeyCode.UP, Direction.UP);
        directionMap.put(KeyCode.DOWN, Direction.DOWN);
        directionMap.put(KeyCode.LEFT, Direction.LEFT);
        directionMap.put(KeyCode.RIGHT, Direction.RIGHT);
    }

    private GameImage heroImage;

    @Override
    public void start(Stage stage) {
        this.createModel();

        Scene scnMain = this.createScene();

        stage.setTitle("Fifteen Puzzle");
        this.setAppIcon(stage, ICON_FILE);
        stage.setScene(scnMain);
        stage.setOnCloseRequest((e) -> {
            System.exit(0);
        });
        stage.show();
    }

    private void createModel() {
        this.model = new Model(this);
        for (int i = 1; i <= 5; i++) {
            System.out.println(model.addNewMonster(i + ""));
        }
    }

    private Scene createScene() {

        VBox vbxMain = new VBox();

        this.startButton = new Button("Start");
        this.startButton.setOnAction(e -> {
            this.model.startTimer();

            Thread t = this.model.moveMonsters(5000);

        });
        this.timeLabel = new Label(this.model.getTimerValue() + "");
        Pane gridUI = this.createGridUI();
        vbxMain.getChildren().addAll(this.startButton, this.timeLabel, gridUI);

        this.addMonsters();

        Scene scnMain = new Scene(vbxMain);
        this.setKeyHandle(scnMain);

        return scnMain;
    }

    private void setAppIcon(Stage stage, String filename) {
        try {
            Image ico = new Image(filename);
            stage.getIcons().add(ico);
        } catch (Exception ex) {
            System.err.println("Error setting icon");
        }
    }

    private Pane createGridUI() {
        this.pane = new Pane();
        for (int line = 0; line < Model.N_LINES; line++) {
            for (int col = 0; col < Model.N_COLS; col++) {
                Position pos = new Position(line, col);
                GameImage pi = new GameImage("background100", pos);
                this.pane.getChildren().add(pi); // add to gui
            }
        }
        this.addMovingImages(pane);
        this.addHero(pane);
        return pane;
    }

    private void addHero(Pane pane) {
        Position pos = this.model.getHero().getPos();
        this.heroImage = new GameImage("background100", pos);
        this.pane.getChildren().add(this.heroImage); // add to gui
    }

    private void addMovingImages(Pane pane) {
        this.movingImages = new HashMap<>();
        List<Monster> monsters = this.model.getMonsters();
        for (Monster m : monsters) {
            Position pos = m.getPos();
            GameImage pi = new GameImage("background100", pos);
            this.pane.getChildren().add(pi); // add to gui
            this.movingImages.put(m.getName(), pi); // add to grid of moving images
        }
    }

    void setKeyHandle(Scene scnMain) {
        scnMain.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                model.moveHeroInDirection(directionMap.get(event.getCode()));
            }
        });
    }

    /**
     * Updates the pieces content by asking the model
     */
    private void addMonsters() {
        List<Monster> monsters = this.model.getMonsters();
        for (Monster m : monsters) {
            Position p = m.getPos();
            GameImage gi = this.movingImages.get(m.getName());
            gi.setImage(m.getName());
            gi.setPositionAndXY(p);
        }
    }

    /**
     * Update GUI after movement of Mobile object
     * @param lastMove
     */
    public void updateLayoutAfterMove(String name, Move lastMove) {
        if (lastMove == null) {
            return;
        } else {
            Platform.runLater(() -> {
                int beginLine = lastMove.getBegin().getLine();
                int beginCol = lastMove.getBegin().getCol();
                int endLine = lastMove.getEnd().getLine();
                int endCol = lastMove.getEnd().getCol();

                GameImage imageToMove = this.movingImages.get(name);
                if (imageToMove != null && !imageToMove.isMoving()) {
                    imageToMove.setIsMoving(true);

                    TranslateTransition tt =
                            new TranslateTransition(Duration.millis(100), imageToMove);
                    int dCol = endCol - beginCol;
                    int dLine = endLine - beginLine;

                    tt.setByX(dCol * GameImage.SIDE_SIZE);
                    tt.setByY(dLine * GameImage.SIDE_SIZE);
                    //tt.setCycleCount(Timeline.INDEFINITE);
                    tt.setOnFinished(e -> {
                        imageToMove.updatePosition(dCol, dLine);
                        imageToMove.setIsMoving(false);
                    });
                    tt.play();
                }
            });
        }
    }


    @Override
    public void notifyView(int[][] board) {
        Platform.runLater(() -> {
            // to do
        });
    }

    @Override
    public void showMonsterPosition(Monster randMonster) {
        // to do
    }

    /**
     * Start program
     *
     * @param args currently not used
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
