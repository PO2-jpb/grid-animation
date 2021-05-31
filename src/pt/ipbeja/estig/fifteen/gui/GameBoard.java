package pt.ipbeja.estig.fifteen.gui;

import java.util.*;

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
    private GameImage[][] gridImages;

    private Label timeLabel;
    private static Map<KeyCode, Direction> directionMap = new HashMap<>();

    static {
        directionMap.put(KeyCode.UP, Direction.UP);
        directionMap.put(KeyCode.DOWN, Direction.DOWN);
        directionMap.put(KeyCode.LEFT, Direction.LEFT);
        directionMap.put(KeyCode.RIGHT, Direction.RIGHT);
    }

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
        this.startButton.setOnAction( e -> {
            this.model.startTimer();

            Thread t = this.model.moveMonsters(5);

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
        int nLines = Model.N_LINES;
        int nCols = Model.N_COLS;
        this.pane = new Pane();
        this.gridImages = new GameImage[nLines][nCols];

        for (int line = 0; line < nLines; line++) {
            for (int col = 0; col < nCols; col++) {
                Position pos = new Position(line, col);
                GameImage pi = new GameImage("background100", pos);
                this.pane.getChildren().add(pi); // add to gui
                this.gridImages[line][col] = pi; // add t grid
            }
        }
        return pane;
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
            GameImage gi = this.gridImages[p.getLine()][p.getCol()];
            gi.setImage(m.getName());
            gi.setPositionAndXY(p);
        }
    }

    public void updateLayoutAfterMove(Move lastMove) {
        if (lastMove == null) {
            return;
        }
        else {
            Platform.runLater(() -> {
                int beginLine = lastMove.getBegin().getLine();
                int beginCol = lastMove.getBegin().getCol();
                int endLine = lastMove.getEnd().getLine();
                int endCol = lastMove.getEnd().getCol();

                GameImage imageToMove = this.gridImages[beginLine][beginCol];
                GameImage imageToReplace = this.gridImages[endLine][endCol];

                TranslateTransition tt =
                        new TranslateTransition(Duration.millis(500), imageToMove);
                int dCol = endCol - beginCol;
                int dLine = endLine - beginLine;
                tt.setByX(dCol * GameImage.SIDE_SIZE);
                tt.setByY(dLine * GameImage.SIDE_SIZE);
                tt.play();

                imageToMove.updatePosition(dCol, dLine);
                this.gridImages[endLine][endCol] = imageToMove;

                imageToReplace.setPositionAndXY(new Position(beginLine, beginCol));
                this.gridImages[beginLine][beginCol] = imageToReplace;
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
