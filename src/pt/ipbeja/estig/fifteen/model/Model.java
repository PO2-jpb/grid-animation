package pt.ipbeja.estig.fifteen.model;

import pt.ipbeja.estig.fifteen.gui.View;

import java.util.*;

/**
 * board model
 *
 * @author João Paulo Barros
 * @version 2021/05/28
 */
public class Model {
    public static final int N_LINES = 20;
    public static final int N_COLS = 20;

    private final static Random RAND = new Random();
    private int[][] board;
    private Set<Position> freePosition;
    private Hero hero;

    private List<Monster> monsters;

    private Timer timer;
    private int timerValue;

    private View view;

    /**
     * Creates board in winning position
     */
    public Model(View view) {

        this.hero = new Hero("Hero", new Position(N_LINES / 2, N_COLS / 2));
        this.timer = new Timer();
        this.view = view;
        this.monsters = new ArrayList<>();
        this.freePosition = new HashSet<>();
        this.resetBoard();
    }

    /**
     * Creates a random mixed board starting from a winning position
     *
     * @param minIter        minimum number of iterations to mix board
     * @param additionalIter maximum number of additional iterations to mix board
     */
    public Model(View view, int minIter, int additionalIter) {
        this(view); // call default constructor Fifteen()
        this.resetTimer();
        this.startTimer();
    }

    public Hero getHero() {
        return this.hero;
    }

    public Monster addNewMonster(String name) {
        Position freePos = this.getRandomFreePos();
        Monster monster = new Monster(name, freePos);
        this.monsters.add(monster);
        return monster;
    }

    public List<Monster> getMonsters() {
        return this.monsters;
    }


    public Thread moveMonsters(int nMovements) {
        Runnable r = () -> {
            for (int i = 0; i < nMovements; i++) {
                int randMonsterPosition = RAND.nextInt(this.monsters.size());
                Monster randMonster = this.monsters.get(randMonsterPosition);
                Position beginPos = randMonster.getPos();
                Direction randomDirection = Direction.values()[RAND.nextInt(Direction.values().length)];
                if (beginPos.isInsideAfter(randomDirection)) {
                    randMonster.move(randomDirection);
                    Position endPos = randMonster.getPos();
                    Move move = new Move(beginPos, endPos);
                    this.view.updateLayoutAfterMove(randMonster.getName(), move);
                    Model.sleep(100);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        return t;
    }

    /**
     * Move hero in specified direction
     *
     * @param direction
     */
    public void moveHeroInDirection(Direction direction) {
        Position neighborPosition = this.hero.getPos().neighborPosition(direction);
        if (this.freePosition.contains(neighborPosition))
            this.hero.moveTo(neighborPosition);
    }

    /**
     * @return fifteen board content in text form
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int line = 0; line < N_LINES; line++) {
            for (int col = 0; col < N_COLS; col++) {
                s.append(String.format("%2d, ", this.board[line][col]));
            }
            s.setLength(s.length() - 2);
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * get piece at given position
     *
     * @param position to get piece
     * @return the piece at position
     */
    public int pieceAt(Position position) {
        return this.board[position.getLine()][position.getCol()];
    }

    /**
     * Get all the pieces in a new list
     *
     * @return list with all pieces (line order)
     */
    public List<Integer> getBoard() {
        List<Integer> list = new ArrayList<>();
        for (int line = 0; line < N_LINES; line++) {
            for (int col = 0; col < N_COLS; col++) {
                list.add(this.board[line][col]);
            }
        }
        return list;
    }

    /**
     * Get a random posiiton that is free from monsters
     *
     * @return the random free position
     */
    private Position getRandomFreePos() {
        Position[] positions = new Position[this.freePosition.size()];
        this.freePosition.toArray(positions);
        return positions[RAND.nextInt(this.freePosition.size())];
    }

    /**
     * Puts the board in the starting position
     */
    private void resetBoard() {
        this.board = new int[Model.N_LINES][Model.N_COLS];
        int pos = 1;
        for (int line = 0; line < Model.N_LINES; line++) {
            for (int col = 0; col < Model.N_COLS; col++) {
                this.board[line][col] = pos++;
                this.freePosition.add(new Position(line, col)); // all are free
            }
        }
    }

    /**
     * Notify views
     */
    private void notifyViews() {
        this.view.notifyView(board);
    }

    /**
     * Wait the specified time in milliseconds
     * More info at  https://stackoverflow.com/questions/26703324/why-do-i-need-to-handle-an-exception-for-thread-sleep
     *
     * @param sleepTime time in miliseconds
     */
    public static void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println("Thread " + Thread.currentThread() + " was interrupted!");
            e.printStackTrace();
        }
    }

    // --------- Timer methods ---------------------

    /**
     * Creates a new timer and sets the timer count to zero
     */
    public void resetTimer() {
        this.timerValue = -1;
        this.timer = new Timer();
    }

    /**
     * Starts timer
     */
    public void startTimer() {
        this.resetTimer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timerValue++;
                notifyViews();
            }
        };
        this.timer.schedule(timerTask, 0, 1000);
    }

    /**
     * Stops the current timer
     */
    public void stopTimer() {
        timer.cancel();
    }

    /**
     * Get current timer value
     *
     * @return time in seconds
     */
    public int getTimerValue() {
        return this.timerValue;
    }

}


//    /**
//     * rewinds the puzzle with given moves and applies the reverse of each move
//     *
//     * @param sleepTime time between each move
//     */
//    public void unmix(int sleepTime) {
//        Runnable task = () -> {
//            Move m;
//            while ((m = moves.poll()) != null) {
//                Move mr = m.getReversed();
//                (/applyMove(mr);
//                FifteenModel.sleep(sleepTime);
//                boolean winning = inWinningPositions();
//
//                notifyViews(mr, winning, timerValue);
//
//                if (winning) {
//                    moves.clear();
//                    break;
//                }
//            }
//        };
//        Thread threadToUnmix = new Thread(task);
//        threadToUnmix.start();
//    }


//    /**
//     * Tries to move a piece at position If moved notifies views
//     *
//     * @param position position of piece to move
//     */
//    private void movePieceAt(Position position) {
//        if (position.isInside()) {
//            Position emptyPos = this.getEmptyInNeighborhood(position);
//            if (emptyPos != null) {
//                Move newMove = new Move(position, emptyPos);
//                this.applyMove(newMove);
//                this.moves.addFirst(newMove); // add at head (begin) of deque
//                boolean winning = inWinningPositions();
//                this.notifyViews(newMove, winning, timerValue);
//                if (winning) {
//                    timerValue = 0;
//                    timer.cancel();
//                }
//            }
//        }
//    }
